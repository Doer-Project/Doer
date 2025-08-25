-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 25, 2025 at 11:59 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `doer`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `AcceptWorkRequest` (IN `p_request_id` INT, IN `p_worker_id` VARCHAR(20), IN `p_proposed_start_time` TIME, IN `p_proposed_end_time` TIME, IN `p_estimated_cost` DECIMAL(10,2), OUT `p_result_status` VARCHAR(50))   BEGIN
    sp: BEGIN
        DECLARE v_conflict_count INT DEFAULT 0;
        DECLARE v_work_date DATE;
        DECLARE done INT DEFAULT FALSE;
        DECLARE v_existing_start TIME;
        DECLARE v_existing_end TIME;
        DECLARE v_current_time TIME;
        DECLARE v_current_date DATE;

        DECLARE conflict_cursor CURSOR FOR
            SELECT at.start_time, at.end_time
            FROM assignedtasks at
            JOIN workrequests wr ON at.request_id = wr.request_id
            WHERE at.worker_id = p_worker_id 
              AND at.status NOT IN ('completed', 'rated')
              AND wr.preferred_work_date = v_work_date
            UNION ALL
            SELECT rr.proposed_start_time, rr.proposed_end_time
            FROM requestrecipients rr
            JOIN workrequests wr ON rr.request_id = wr.request_id
            WHERE rr.worker_id = p_worker_id 
              AND rr.interest_status = 'interested'
              AND rr.request_id != p_request_id
              AND wr.preferred_work_date = v_work_date;

        DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

        -- Current date/time
        SET v_current_date = CURDATE();
        SET v_current_time = CURTIME();

        -- Get work date
        SELECT preferred_work_date 
        INTO v_work_date
        FROM workrequests 
        WHERE request_id = p_request_id;

        -- Time validation for today's date
        IF v_work_date = v_current_date THEN
            IF p_proposed_start_time < v_current_time OR p_proposed_end_time < v_current_time THEN
                SET p_result_status = 'INVALID_TIME';
                LEAVE sp;
            END IF;
        END IF;

        -- Validate time range
        IF p_proposed_start_time >= p_proposed_end_time THEN
            SET p_result_status = 'INVALID_TIME_RANGE';
            LEAVE sp;
        END IF;

        OPEN conflict_cursor;

        check_conflicts:LOOP
            FETCH conflict_cursor INTO v_existing_start, v_existing_end;
            IF done THEN
                LEAVE check_conflicts;
            END IF;

            -- Overlap check
            IF (p_proposed_start_time >= v_existing_start AND p_proposed_start_time < v_existing_end)
               OR (p_proposed_end_time > v_existing_start AND p_proposed_end_time <= v_existing_end)
               OR (p_proposed_start_time <= v_existing_start AND p_proposed_end_time >= v_existing_end)
               OR (p_proposed_start_time = v_existing_start AND p_proposed_end_time = v_existing_end) THEN
                SET v_conflict_count = v_conflict_count + 1;
                LEAVE check_conflicts;
            END IF;
        END LOOP check_conflicts;

        CLOSE conflict_cursor;

        -- Update or return conflict status
        IF v_conflict_count = 0 THEN
            UPDATE requestrecipients 
            SET 
                interest_status = 'interested',
                proposed_start_time = p_proposed_start_time,
                proposed_end_time = p_proposed_end_time,
                estimated_cost = p_estimated_cost,
                responded_at = CURRENT_TIMESTAMP
            WHERE request_id = p_request_id 
              AND worker_id = p_worker_id;

            SET p_result_status = 'SUCCESS';
        ELSE
            SET p_result_status = 'TIME_CONFLICT';
        END IF;

    END sp;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `assign_chosen_worker` (IN `req_id` INT, IN `chosen_worker_id` VARCHAR(20), IN `start_time` TIME, IN `end_time` TIME, IN `cost` DECIMAL(10,2))   BEGIN
    DECLARE h_id VARCHAR(20);
    DECLARE h_name VARCHAR(50);

    -- Get household_id from the request
    SELECT household_id INTO h_id
    FROM workrequests
    WHERE request_id = req_id;

    -- Insert chosen worker into assignedtasks
    INSERT INTO assignedtasks (
        request_id, household_id, worker_id, start_time, end_time, cost, status
    ) VALUES (
        req_id, h_id, chosen_worker_id, start_time, end_time, cost, 'upcoming'
    );

    -- Mark request as Assigned
    UPDATE workrequests
    SET status = 'Assigned'
    WHERE request_id = req_id;

    -- Notify chosen worker
    INSERT INTO notifications (receiver_id, message, sender_name)
    VALUES (chosen_worker_id, CONCAT('You have been assigned a new work request'));

    -- Notify other workers who were recipients
    INSERT INTO notifications (receiver_id, message, sender_name)
    SELECT worker_id,
           CONCAT('Work request (ID: ', req_id, ') has been assigned to another worker.')
    FROM requestrecipients
    WHERE request_id = req_id
      AND worker_id <> chosen_worker_id;

    -- Delete all requestrecipients entries for this request
    DELETE FROM requestrecipients
    WHERE request_id = req_id;
END$$

DELIMITER ;

-- ... (rest of the SQL file as above) ...
