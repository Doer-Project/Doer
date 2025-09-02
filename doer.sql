-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Sep 02, 2025 at 05:37 PM
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
    INSERT INTO notifications (receiver_id, message)
    VALUES (chosen_worker_id, CONCAT('You have been assigned a new work request'));

    -- Notify other workers who were recipients
    INSERT INTO notifications (receiver_id, message)
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

-- --------------------------------------------------------

--
-- Table structure for table `assignedtasks`
--

CREATE TABLE `assignedtasks` (
  `task_id` int(11) NOT NULL,
  `request_id` int(11) DEFAULT NULL,
  `household_id` varchar(20) DEFAULT NULL,
  `worker_id` varchar(20) DEFAULT NULL,
  `start_time` time DEFAULT NULL,
  `end_time` time DEFAULT NULL,
  `cost` decimal(10,2) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `completed_at` timestamp NULL DEFAULT NULL,
  `household_rating` int(11) DEFAULT NULL,
  `household_review` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `assignedtasks`
--

INSERT INTO `assignedtasks` (`task_id`, `request_id`, `household_id`, `worker_id`, `start_time`, `end_time`, `cost`, `status`, `completed_at`, `household_rating`, `household_review`) VALUES
(4, 20, 'H100', 'W102', '09:00:00', '10:00:00', 11111.00, 'upcoming', NULL, NULL, NULL),
(5, 22, 'H100', 'W102', '12:00:00', '14:00:00', 78.00, 'upcoming', NULL, NULL, NULL),
(6, 23, 'H100', 'W102', '09:00:00', '17:00:00', 700.00, 'rated', NULL, 1, 'jk'),
(8, 24, 'H100', 'W102', '16:00:00', '17:00:00', 500.00, 'rated', NULL, 3, 'skdjkjfbf'),
(9, 24, 'H201', 'W200', '10:00:00', '14:00:00', 2500.00, 'rated', NULL, 2, NULL),
(10, 25, 'H100', 'W202', '08:00:00', '12:00:00', 1800.00, 'rated', NULL, 4, NULL),
(11, 31, 'H100', 'W309', '16:00:00', '17:00:00', 300.00, 'COMPLETED', NULL, NULL, NULL),
(12, 28, 'H100', 'W202', '09:00:00', '17:00:00', 500.00, 'upcoming', NULL, NULL, NULL),
(13, 33, 'HAHM25HH0030', 'W201', '23:50:00', '23:59:00', 500.00, 'COMPLETED', NULL, NULL, NULL),
(14, 32, 'H100', 'W102', '23:54:00', '23:59:00', 12.00, 'COMPLETED', NULL, NULL, NULL),
(15, 34, 'H100', 'W101', '09:00:00', '17:00:00', 500.00, 'COMPLETED', NULL, NULL, NULL),
(16, 35, 'HAHM25HH0005', 'W313', '10:00:00', '11:00:00', 350.00, 'upcoming', NULL, NULL, NULL),
(17, 36, 'HAHM25HH0006', 'W101', '09:30:00', '10:30:00', 450.00, 'rated', NULL, 2, 'sgnds'),
(18, 37, 'HAHM25HH0007', 'W501', '14:00:00', '16:00:00', 900.00, 'upcoming', NULL, NULL, NULL),
(19, 38, 'HAHM25HH0008', 'W304', '12:00:00', '13:30:00', 700.00, 'upcoming', NULL, NULL, NULL),
(20, 39, 'HAHM25HH0009', 'W309', '08:00:00', '10:00:00', 600.00, 'upcoming', NULL, NULL, NULL),
(21, 40, 'HAHM25HH0010', 'WAHM25PA0003', '10:00:00', '13:00:00', 2500.00, 'upcoming', NULL, NULL, NULL),
(22, 41, 'H100', 'W300', '11:00:00', '12:00:00', 400.00, 'upcoming', NULL, NULL, NULL),
(23, 35, 'HAHM25HH0005', 'W313', '10:00:00', '11:00:00', 350.00, 'rated', '2025-08-26 01:30:00', 1, NULL),
(24, 36, 'HAHM25HH0006', 'W101', '09:30:00', '10:30:00', 450.00, 'COMPLETED', '2025-08-27 00:00:00', 4, 'Leak fixed'),
(25, 37, 'HAHM25HH0007', 'W501', '14:00:00', '16:00:00', 900.00, 'COMPLETED', '2025-08-28 06:10:00', 5, 'Very thorough'),
(26, 42, 'H100', 'W102', '16:00:00', '17:00:00', 12.00, 'completed', '2025-08-23 07:52:22', NULL, NULL),
(27, 42, 'H100', 'W102', '16:00:00', '17:00:00', 12.00, 'completed', '2025-08-23 07:53:46', NULL, NULL),
(28, 30, 'H100', 'W201', '09:00:00', '17:00:00', 345.00, 'upcoming', NULL, NULL, NULL),
(29, 30, 'H100', 'W201', '09:00:00', '17:00:00', 345.00, 'upcoming', NULL, NULL, NULL),
(30, 21, 'H100', 'W101', '09:00:00', '17:00:00', 23.00, 'upcoming', NULL, NULL, NULL),
(31, 35, 'HAHM25HH0005', 'W102', '09:00:00', '17:00:00', 123.00, 'upcoming', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Stand-in structure for view `futurework`
-- (See below for the actual view)
--
CREATE TABLE `futurework` (
`task_id` int(11)
,`request_id` int(11)
,`title` varchar(255)
,`address` text
,`household_id` varchar(20)
,`worker_id` varchar(20)
,`date` date
,`start_time` time
,`end_time` time
,`cost` decimal(10,2)
,`status` varchar(20)
);

-- --------------------------------------------------------

--
-- Table structure for table `households`
--

CREATE TABLE `households` (
  `household_id` varchar(20) NOT NULL,
  `address` text DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `pincode` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `households`
--

INSERT INTO `households` (`household_id`, `address`, `city`, `pincode`) VALUES
('H100', '39, Nikol, Ahmedabad', 'Ahmedabad', 382350),
('H200', '45, Satellite Road', 'Ahmedabad', 380015),
('H201', '12, SG Highway', 'Ahmedabad', 380060),
('HAHM25HH0004', 'papas home 11', 'ahmedabad', 380009),
('HAHM25HH0005', '11, CG Road', 'Ahmedabad', 380009),
('HAHM25HH0006', '22, Prahladnagar', 'Ahmedabad', 380015),
('HAHM25HH0007', '301, Vastrapur', 'Ahmedabad', 380052),
('HAHM25HH0008', '7, Maninagar', 'Ahmedabad', 380008),
('HAHM25HH0009', '12, Bopal', 'Ahmedabad', 380058),
('HAHM25HH0010', '55, Navrangpura', 'Ahmedabad', 380009),
('HAHM25HH0030', 'f', 'Ahmedabad', 123456),
('HAHM25HH0044', 'kesar harmont', 'ahmedaabad', 382350),
('HAHM25HH0045', 'hirawadi', 'Ahmedabad', 785412),
('HMUM25HH0001', '101, Marine Drive, Mumbai', 'Mumbai', 400020),
('HMUM25HH0002', '21, Andheri West', 'Mumbai', 400053),
('HMUM25HH0003', '14, Powai', 'Mumbai', 400076),
('HMUM25HH0004', '9, Dadar East', 'Mumbai', 400014);

-- --------------------------------------------------------

--
-- Table structure for table `notifications`
--

CREATE TABLE `notifications` (
  `notif_id` int(11) NOT NULL,
  `receiver_id` varchar(20) DEFAULT NULL,
  `message` text DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `notifications`
--

INSERT INTO `notifications` (`notif_id`, `receiver_id`, `message`, `created_at`) VALUES
(1, 'H100', 'Your plumbing request has been received.', '2025-08-17 04:25:45'),
(2, 'W101', 'You have a new plumbing task opportunity.', '2025-08-17 04:25:45'),
(3, 'H100', 'Your plumbing request is recorded for 22-08-2025.', '2025-08-22 10:12:45'),
(4, 'W101', 'New plumbing job opportunity available for 22-08-2025.', '2025-08-22 10:12:45'),
(5, 'H200', 'Cleaner request has been created for 22-08-2025.', '2025-08-22 10:12:45'),
(6, 'W201', 'New cleaning job opportunity available for 22-08-2025.', '2025-08-22 10:12:45'),
(7, 'H100', 'Work is completed please give review', '2025-08-22 13:11:39'),
(8, 'HAHM25HH0005', 'Your request (Fan Repair) has been created.', '2025-08-22 19:00:00'),
(9, 'W313', 'New electrician job: Request #35', '2025-08-22 19:00:05'),
(10, 'HAHM25HH0006', 'Your request (Leaky Tap) has been created.', '2025-08-22 19:01:00'),
(11, 'W101', 'New plumbing job: Request #36', '2025-08-22 19:01:05'),
(12, 'HAHM25HH0007', 'Your request (Sofa Cleaning) has been created.', '2025-08-22 19:02:00'),
(13, 'W501', 'New cleaning job: Request #37', '2025-08-22 19:02:05'),
(14, 'HAHM25HH0008', 'Your request (Window Fix) has been created.', '2025-08-22 19:03:00'),
(15, 'W304', 'New carpentry job: Request #38', '2025-08-22 19:03:05'),
(16, 'HAHM25HH0009', 'Your request (Garden Trim) has been created.', '2025-08-22 19:04:00'),
(17, 'W309', 'New gardening job: Request #39', '2025-08-22 19:04:05'),
(18, 'HAHM25HH0010', 'Your request (Bedroom Paint) has been created.', '2025-08-22 19:05:00'),
(19, 'WAHM25PA0003', 'New painting job: Request #40', '2025-08-22 19:05:05'),
(20, 'H100', 'Your request (Washing Machine Socket) has been created.', '2025-08-22 19:06:00'),
(21, 'W300', 'New electrician job: Request #41', '2025-08-22 19:06:05'),
(22, 'HAHM25HH0006', 'Work is completed please give review', '2025-08-22 23:24:44'),
(23, 'H100', 'Work is completed please give review', '2025-08-23 07:52:25'),
(24, 'H100', 'Work is completed please give review', '2025-08-23 07:53:48'),
(25, 'W201', 'You have been assigned a new work request', '2025-09-02 14:45:01'),
(26, 'W101', 'You have been assigned a new work request', '2025-09-02 14:48:24'),
(27, 'W102', 'You have been assigned a new work request', '2025-09-02 15:08:47'),
(28, 'W313', 'Work request (ID: 35) has been assigned to another worker.', '2025-09-02 15:08:47'),
(29, 'W302', 'Work request (ID: 35) has been assigned to another worker.', '2025-09-02 15:08:47'),
(30, 'W303', 'Work request (ID: 35) has been assigned to another worker.', '2025-09-02 15:08:47');

-- --------------------------------------------------------

--
-- Stand-in structure for view `pastwork`
-- (See below for the actual view)
--
CREATE TABLE `pastwork` (
`task_id` int(11)
,`request_id` int(11)
,`worker_id` varchar(20)
,`household_id` varchar(20)
,`title` varchar(255)
,`date` date
,`rating` int(11)
,`review` text
,`cost` decimal(10,2)
,`status` varchar(20)
);

-- --------------------------------------------------------

--
-- Table structure for table `requestrecipients`
--

CREATE TABLE `requestrecipients` (
  `recipient_id` int(11) NOT NULL,
  `request_id` int(11) DEFAULT NULL,
  `worker_id` varchar(20) DEFAULT NULL,
  `interest_status` varchar(20) DEFAULT 'pending',
  `proposed_start_time` time DEFAULT NULL,
  `proposed_end_time` time DEFAULT NULL,
  `estimated_cost` decimal(10,2) DEFAULT NULL,
  `responded_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `requestrecipients`
--

INSERT INTO `requestrecipients` (`recipient_id`, `request_id`, `worker_id`, `interest_status`, `proposed_start_time`, `proposed_end_time`, `estimated_cost`, `responded_at`) VALUES
(14, 25, 'W101', 'interested', '09:00:00', '17:00:00', 23.00, '2025-09-02 14:47:49'),
(15, 26, 'W201', 'pending', NULL, NULL, NULL, '2025-08-22 10:12:45'),
(16, 27, 'W200', 'pending', NULL, NULL, NULL, '2025-08-22 10:12:45'),
(18, 29, 'W201', 'pending', NULL, NULL, NULL, '2025-08-22 10:12:45'),
(19, 30, 'W503', 'pending', NULL, NULL, NULL, '2025-08-22 11:13:02'),
(20, 30, 'W201', 'interested', '09:00:00', '17:00:00', 345.00, '2025-09-02 14:32:43'),
(21, 30, 'W307', 'pending', NULL, NULL, NULL, '2025-08-22 11:13:02'),
(22, 30, 'W501', 'pending', NULL, NULL, NULL, '2025-08-22 11:13:02'),
(23, 30, 'W500', 'pending', NULL, NULL, NULL, '2025-08-22 11:13:02'),
(24, 30, 'W502', 'pending', NULL, NULL, NULL, '2025-08-22 11:13:02'),
(25, 30, 'W306', 'pending', NULL, NULL, NULL, '2025-08-22 11:13:02'),
(26, 30, 'W504', 'pending', NULL, NULL, NULL, '2025-08-22 11:13:02'),
(70, 36, 'W312', 'pending', NULL, NULL, NULL, '2025-08-22 23:19:33'),
(71, 36, 'W101', 'pending', NULL, NULL, NULL, '2025-08-22 23:19:33'),
(72, 36, 'W301', 'pending', NULL, NULL, NULL, '2025-08-22 23:19:33'),
(73, 36, 'W300', 'pending', NULL, NULL, NULL, '2025-08-22 23:19:33'),
(77, 37, 'W503', 'pending', NULL, NULL, NULL, '2025-08-22 23:19:33'),
(78, 37, 'W201', 'pending', NULL, NULL, NULL, '2025-08-22 23:19:33'),
(79, 37, 'W307', 'pending', NULL, NULL, NULL, '2025-08-22 23:19:33'),
(80, 37, 'W501', 'pending', NULL, NULL, NULL, '2025-08-22 23:19:33'),
(81, 37, 'W500', 'pending', NULL, NULL, NULL, '2025-08-22 23:19:33'),
(82, 37, 'W502', 'pending', NULL, NULL, NULL, '2025-08-22 23:19:33'),
(83, 37, 'W306', 'pending', NULL, NULL, NULL, '2025-08-22 23:19:33'),
(84, 37, 'W504', 'pending', NULL, NULL, NULL, '2025-08-22 23:19:33'),
(92, 38, 'W200', 'pending', NULL, NULL, NULL, '2025-08-22 23:19:33'),
(93, 38, 'W304', 'pending', NULL, NULL, NULL, '2025-08-22 23:19:33'),
(94, 38, 'W314', 'pending', NULL, NULL, NULL, '2025-08-22 23:19:33'),
(95, 38, 'W305', 'pending', NULL, NULL, NULL, '2025-08-22 23:19:33'),
(99, 39, 'W202', 'pending', NULL, NULL, NULL, '2025-08-22 23:19:33'),
(100, 39, 'W308', 'pending', NULL, NULL, NULL, '2025-08-22 23:19:33'),
(101, 39, 'W309', 'pending', NULL, NULL, NULL, '2025-08-22 23:19:33'),
(102, 40, 'W310', 'pending', NULL, NULL, NULL, '2025-08-22 23:19:33'),
(103, 40, 'W311', 'pending', NULL, NULL, NULL, '2025-08-22 23:19:33'),
(104, 40, 'WAHM25PA0003', 'pending', NULL, NULL, NULL, '2025-08-22 23:19:33'),
(105, 41, 'W313', 'pending', NULL, NULL, NULL, '2025-08-22 23:19:33'),
(106, 41, 'W302', 'pending', NULL, NULL, NULL, '2025-08-22 23:19:33'),
(107, 41, 'W102', 'not interested', NULL, NULL, NULL, '2025-08-22 23:19:33'),
(108, 41, 'W303', 'pending', NULL, NULL, NULL, '2025-08-22 23:19:33'),
(109, 42, 'W313', 'pending', NULL, NULL, NULL, '2025-08-23 06:02:02'),
(110, 42, 'W302', 'pending', NULL, NULL, NULL, '2025-08-23 06:02:02'),
(111, 42, 'W303', 'pending', NULL, NULL, NULL, '2025-08-23 06:02:02'),
(112, 42, 'W102', 'interested', '16:00:00', '17:00:00', 12.00, '2025-08-23 06:06:11'),
(116, 43, 'W312', 'pending', NULL, NULL, NULL, '2025-08-23 10:31:27'),
(117, 43, 'W301', 'pending', NULL, NULL, NULL, '2025-08-23 10:31:27'),
(118, 43, 'W300', 'pending', NULL, NULL, NULL, '2025-08-23 10:31:27'),
(119, 43, 'W101', 'pending', NULL, NULL, NULL, '2025-08-23 10:31:27'),
(120, 43, 'WAHM25PL0043', 'pending', NULL, NULL, NULL, '2025-08-23 10:31:27');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` varchar(20) NOT NULL,
  `role` enum('household','worker') NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `age` int(11) DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `prof_pic` longblob DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `role`, `first_name`, `last_name`, `email`, `password_hash`, `age`, `gender`, `prof_pic`) VALUES
('H100', 'household', 'Nihar', 'Kakani', 'niharpatel2106@gmail.com', 'Safe@123', 18, 'Male', NULL),
('H200', 'household', 'Priya', 'Sharma', 'priya.sharma@example.com', 'Safe@123', 26, 'Female', NULL),
('H201', 'household', 'Raj', 'Patel', 'raj.patel@example.com', 'Safe@123', 32, 'Male', NULL),
('HAHM25HH0004', 'household', 'niti', 'radadiya', 'nithr007@gmail.com', 'Safe@1234', 18, 'Female', NULL),
('HAHM25HH0005', 'household', 'Isha', 'Desai', 'isha.desai@example.com', 'Safe@123', 29, 'Female', NULL),
('HAHM25HH0006', 'household', 'Manav', 'Shah', 'manavshahhh0006@example.com', 'Safe@123', 34, 'Male', NULL),
('HAHM25HH0007', 'household', 'Riddhi', 'Patel', 'riddhi.patel+hh0007@example.com', 'Safe@123', 27, 'Female', NULL),
('HAHM25HH0008', 'household', 'Parth', 'Mehta', 'parth.mehta+hh0008@example.com', 'Safe@123', 31, 'Male', NULL),
('HAHM25HH0009', 'household', 'Dhwani', 'Trivedi', 'dhwani.trivedi+hh0009@example.com', 'Safe@123', 26, 'Female', NULL),
('HAHM25HH0010', 'household', 'Harsh', 'Joshi', 'harsh.joshi+hh0010@example.com', 'Safe@123', 33, 'Male', NULL),
('HAHM25HH0030', 'household', 'Dharmil', 'Panchal', 'dharmil132@gmail.com', 'Dharmil@123', 18, 'Male', NULL),
('HAHM25HH0044', 'household', 'jems@', 'patoliya', 'jemspatoliya03@gmail.com', 'Safe@123', 19, 'Male', NULL),
('HAHM25HH0045', 'household', 'Yash', 'Kotadiya', 'yashisyash22@gmail.com', 'Safe@123', 18, 'Male', NULL),
('HMUM25HH0001', 'household', 'Kavita', 'Singh', 'kavita.singh@example.com', 'Passw0rd123!', 45, 'Female', NULL),
('HMUM25HH0002', 'household', 'Rohit', 'Kumar', 'rohit.kumar+hh0002@example.com', 'Safe@123', 36, 'Male', NULL),
('HMUM25HH0003', 'household', 'Seema', 'Nair', 'seema.nair+hh0003@example.com', 'Safe@123', 42, 'Female', NULL),
('HMUM25HH0004', 'household', 'Arjun', 'Iyer', 'arjun.iyer+hh0004@example.com', 'Safe@123', 38, 'Male', NULL),
('W101', 'worker', 'Amit', 'Mehta', 'amit.mehta@example.com', 'hashpass1', 30, 'Male', NULL),
('W102', 'worker', 'Sunita', 'Verma', 'sunita.verma@example.com', 'hashpass2', 28, 'Female', NULL),
('W200', 'worker', 'Karan', 'Shah', 'karan.shah@example.com', 'Safe@123', 29, 'Male', NULL),
('W201', 'worker', 'Meena', 'Joshi', 'meena.joshi@example.com', 'Safe@123', 35, 'Female', NULL),
('W202', 'worker', 'Ravi', 'Patel', 'ravi.patel@example.com', 'Safe@123', 40, 'Male', NULL),
('W300', 'worker', 'Rohit', 'Patel', 'rohit.patel@example.com', 'Safe@123', 27, 'Male', NULL),
('W301', 'worker', 'Sneha', 'Shah', 'sneha.shah@example.com', 'Safe@123', 24, 'Female', NULL),
('W302', 'worker', 'Vikram', 'Thakur', 'vikram.thakur@example.com', 'Safe@123', 33, 'Male', NULL),
('W303', 'worker', 'Manisha', 'Dave', 'manisha.dave@example.com', 'Safe@123', 29, 'Female', NULL),
('W304', 'worker', 'Ajay', 'Joshi', 'ajay.joshi@example.com', 'Safe@123', 35, 'Male', NULL),
('W305', 'worker', 'Pooja', 'Desai', 'pooja.desai@example.com', 'Safe@123', 28, 'Female', NULL),
('W306', 'worker', 'Deep', 'Chauhan', 'deep.chauhan@example.com', 'Safe@123', 31, 'Male', NULL),
('W307', 'worker', 'Rupal', 'Mehta', 'rupal.mehta@example.com', 'Safe@123', 26, 'Female', NULL),
('W308', 'worker', 'Ankit', 'Kapoor', 'ankit.kapoor@example.com', 'Safe@123', 30, 'Male', NULL),
('W309', 'worker', 'Hetal', 'Brahmbhat', 'hetal.brahmbhatt@example.com', 'Safe@123', 32, 'Female', NULL),
('W310', 'worker', 'Suresh', 'Prajapati', 'suresh.prajapati@example.com', 'Safe@123', 37, 'Male', NULL),
('W311', 'worker', 'Payal', 'Trivedi', 'payal.trivedi@example.com', 'Safe@123', 25, 'Female', NULL),
('W312', 'worker', 'Aakash', 'Rathod', 'aakash.rathod@example.com', 'Safe@123', 34, 'Male', NULL),
('W313', 'worker', 'Mehul', 'Solanki', 'mehul.solanki@example.com', 'Safe@123', 38, 'Male', NULL),
('W314', 'worker', 'Bhavna', 'Parmar', 'bhavna.parmar@example.com', 'Safe@123', 27, 'Female', NULL),
('W500', 'worker', 'Neha', 'Sharma', 'neha.sharma@example.com', 'Safe@123', 28, 'Female', NULL),
('W501', 'worker', 'Kunal', 'Desai', 'kunal.desai@example.com', 'Safe@123', 30, 'Male', NULL),
('W502', 'worker', 'Priti', 'Patel', 'priti.patel@example.com', 'Safe@123', 26, 'Female', NULL),
('W503', 'worker', 'Vivek', 'Shah', 'vivek.shah@example.com', 'Safe@123', 32, 'Male', NULL),
('W504', 'worker', 'Megha', 'Kapoor', 'megha.kapoor@example.com', 'Safe@123', 27, 'Female', NULL),
('WAHM25PA0003', 'worker', 'Binal', 'Kakani', 'binalkakani@gmail.com', 'Safe@123', 28, 'Female', NULL),
('WAHM25PL0043', 'worker', 'priyansh', 'vekariya', 'priyanshvekariya08@gmail.com', 'Priyansh@123', 18, 'Male', NULL),
('WMUM25PL0002', 'worker', 'Ramesh', 'Shinde', 'ramesh.shinde@example.com', 'SecurePass!201', 35, 'Male', NULL),
('WMUM25PL0003', 'worker', 'Ganesh', 'Kadam', 'ganesh.kadam@example.com', 'SecurePass!202', 32, 'Male', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `workers`
--

CREATE TABLE `workers` (
  `worker_id` varchar(20) NOT NULL,
  `category` varchar(50) DEFAULT NULL,
  `work_area` varchar(255) DEFAULT NULL,
  `experience_years` int(11) DEFAULT NULL,
  `rating_avg` decimal(2,1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `workers`
--

INSERT INTO `workers` (`worker_id`, `category`, `work_area`, `experience_years`, `rating_avg`) VALUES
('W101', 'Plumber', 'Ahmedabad', 5, 3.0),
('W102', 'Electrician', 'Ahmedabad', 3, 2.0),
('W200', 'Carpenter', 'Ahmedabad', 6, 2.0),
('W201', 'Cleaner', 'Ahmedabad', 2, 4.3),
('W202', 'Gardener', 'Ahmedabad', 8, 4.0),
('W300', 'Plumber', 'Ahmedabad', 4, 4.2),
('W301', 'Plumber', 'Ahmedabad', 6, 4.5),
('W302', 'Electrician', 'Ahmedabad', 5, 4.4),
('W303', 'Electrician', 'Ahmedabad', 3, 4.1),
('W304', 'Carpenter', 'Ahmedabad', 7, 4.6),
('W305', 'Carpenter', 'Ahmedabad', 2, 4.0),
('W306', 'Cleaner', 'Ahmedabad', 1, 3.9),
('W307', 'Cleaner', 'Ahmedabad', 4, 4.3),
('W308', 'Gardener', 'Ahmedabad', 5, 4.8),
('W309', 'Gardener', 'Ahmedabad', 6, 4.7),
('W310', 'Painter', 'Ahmedabad', 4, 4.1),
('W311', 'Painter', 'Ahmedabad', 2, 3.8),
('W312', 'Plumber', 'Ahmedabad', 8, 4.9),
('W313', 'Electrician', 'Ahmedabad', 9, 1.0),
('W314', 'Carpenter', 'Ahmedabad', 3, 4.2),
('W500', 'Cleaner', 'Ahmedabad', 3, 4.1),
('W501', 'Cleaner', 'Ahmedabad', 5, 4.3),
('W502', 'Cleaner', 'Ahmedabad', 2, 4.0),
('W503', 'Cleaner', 'Ahmedabad', 7, 4.7),
('W504', 'Cleaner', 'Ahmedabad', 1, 3.8),
('WAHM25PA0003', 'Painter', 'Ahmedabad', 4, 0.0),
('WAHM25PL0043', 'Plumber', 'Ahmedabad', 100, 0.0);

-- --------------------------------------------------------

--
-- Table structure for table `workrequests`
--

CREATE TABLE `workrequests` (
  `request_id` int(11) NOT NULL,
  `household_id` varchar(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `category` varchar(50) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `preferred_work_date` date DEFAULT NULL,
  `address` text DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `pincode` int(11) DEFAULT NULL,
  `status` varchar(20) DEFAULT 'Pending'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `workrequests`
--

INSERT INTO `workrequests` (`request_id`, `household_id`, `title`, `category`, `description`, `preferred_work_date`, `address`, `city`, `pincode`, `status`) VALUES
(20, 'H100', 'Title1', 'Electrician', 'task1', '2025-08-30', 'area1', 'Ahmedabad', 111111, 'Assigned'),
(21, 'H100', 'kdsjfh', 'Plumber', 'adkfj', '2025-08-29', 'dmnb', 'Ahmedabad', 874512, 'Assigned'),
(22, 'H100', 'title2', 'Electrician', 'cooking	', '2025-09-03', 'kdfvbv', 'Ahmedabad', 874512, 'Assigned'),
(23, 'H100', 'title3', 'Electrician', 'df', '2025-08-22', 'sdg', 'Ahmedabad', 784512, 'rated'),
(24, 'H100', 'TV', 'Electrician', 'Tv is not working, prefferd time is 2 - 3 PM', '2025-08-22', 'New Ranip', 'Ahmedabad', 382480, 'rated'),
(25, 'H100', 'Kitchen Tap Fix', 'Plumber', 'Leaking kitchen tap needs urgent fix.', '2025-08-22', '39, Nikol', 'Ahmedabad', 382350, 'rated'),
(26, 'H200', 'Garden Cleaning', 'Cleaner', 'Clean backyard and garage area.', '2025-08-22', '45, Satellite Road', 'Ahmedabad', 380015, 'Pending'),
(27, 'H201', 'Custom Wardrobe Build', 'Carpenter', 'Need a wooden wardrobe built in bedroom.', '2025-08-25', '12, SG Highway', 'Ahmedabad', 380060, 'Pending'),
(28, 'H100', 'Lawn Maintenance', 'Gardener', 'Trim and clean front lawn area.', '2025-09-05', '39, Nikol', 'Ahmedabad', 382350, 'Assigned'),
(29, 'H200', 'Living Room Deep Clean', 'Cleaner', 'Deep cleaning for Diwali prep.', '2025-09-10', '45, Satellite Road', 'Ahmedabad', 380015, 'Pending'),
(30, 'H100', 'Cleaner Needed', 'Cleaner', 'Full house cleaning service', '2025-08-25', '39, Nikol', 'Ahmedabad', 382350, 'Assigned'),
(31, 'H100', 'Grass cutting', 'Gardener', 'Grass grow too much ', '2025-08-22', 'mmm', 'Ahmedabad', 382480, 'Assigned'),
(32, 'H100', 'sf', 'Electrician', 'fvgb', '2025-08-22', 'fvvsgb', 'Ahmedabad', 323232, 'Assigned'),
(33, 'HAHM25HH0030', 'Clean', 'Cleaner', 'need cleaner', '2025-08-22', 'New Ranip', 'Ahmedabad', 382481, 'Assigned'),
(34, 'H100', 'Kitchen Sink Leakage', 'Plumber', 'Leak in the kitchen sink pipe', '2025-08-23', '39, Nikol, Ahmedabad', 'Ahmedabad', 382350, 'Assigned'),
(35, 'HAHM25HH0005', 'Fan Repair', 'Electrician', 'Ceiling fan making noise', '2025-08-26', '11, CG Road', 'Ahmedabad', 380009, 'rated'),
(36, 'HAHM25HH0006', 'Leaky Tap', 'Plumber', 'Kitchen tap leakage', '2025-08-23', '22, Prahladnagar', 'Ahmedabad', 380015, 'rated'),
(37, 'HAHM25HH0007', 'Sofa Cleaning', 'Cleaner', 'Deep clean living room sofa', '2025-08-28', '301, Vastrapur', 'Ahmedabad', 380052, 'Pending'),
(38, 'HAHM25HH0008', 'Window Fix', 'Carpenter', 'Window hinge replacement', '2025-08-29', '7, Maninagar', 'Ahmedabad', 380008, 'Pending'),
(39, 'HAHM25HH0009', 'Garden Trim', 'Gardener', 'Trim front lawn and hedges', '2025-08-30', '12, Bopal', 'Ahmedabad', 380058, 'Pending'),
(40, 'HAHM25HH0010', 'Bedroom Paint', 'Painter', 'Repaint master bedroom', '2025-09-01', '55, Navrangpura', 'Ahmedabad', 380009, 'Pending'),
(41, 'H100', 'Washing Machine Socket', 'Electrician', 'Socket heats during use', '2025-08-27', '39, Nikol, Ahmedabad', 'Ahmedabad', 382350, 'Pending'),
(42, 'H100', 'title', 'Electrician', 'g', '2025-08-23', 'nikol', 'Ahmedabad', 124578, 'completed'),
(43, 'HAHM25HH0044', '87ty', 'Plumber', 'tgvuy', '2025-08-23', 'iyg', 'Ahmedabad', 147258, 'Pending');

--
-- Triggers `workrequests`
--
DELIMITER $$
CREATE TRIGGER `afterWorkrequestInsert` AFTER INSERT ON `workrequests` FOR EACH ROW INSERT INTO requestrecipients (request_id, worker_id, interest_status)
SELECT NEW.request_id, w.worker_id, 'pending'
FROM workers AS w
WHERE w.category = NEW.category
  AND w.work_area = NEW.city
ORDER BY w.rating_avg DESC
LIMIT 10
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Structure for view `futurework`
--
DROP TABLE IF EXISTS `futurework`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `futurework`  AS SELECT `at`.`task_id` AS `task_id`, `wr`.`request_id` AS `request_id`, `wr`.`title` AS `title`, `wr`.`address` AS `address`, `wr`.`household_id` AS `household_id`, `at`.`worker_id` AS `worker_id`, `wr`.`preferred_work_date` AS `date`, `at`.`start_time` AS `start_time`, `at`.`end_time` AS `end_time`, `at`.`cost` AS `cost`, `at`.`status` AS `status` FROM (`workrequests` `wr` join `assignedtasks` `at` on(`wr`.`request_id` = `at`.`request_id`)) WHERE `at`.`status` = 'upcoming' OR `at`.`status` = 'completed' ;

-- --------------------------------------------------------

--
-- Structure for view `pastwork`
--
DROP TABLE IF EXISTS `pastwork`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `pastwork`  AS SELECT `at`.`task_id` AS `task_id`, `at`.`request_id` AS `request_id`, `at`.`worker_id` AS `worker_id`, `at`.`household_id` AS `household_id`, `wr`.`title` AS `title`, `wr`.`preferred_work_date` AS `date`, `at`.`household_rating` AS `rating`, `at`.`household_review` AS `review`, `at`.`cost` AS `cost`, `at`.`status` AS `status` FROM (`assignedtasks` `at` join `workrequests` `wr` on(`at`.`request_id` = `wr`.`request_id`)) WHERE `at`.`status` = 'rated' ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `assignedtasks`
--
ALTER TABLE `assignedtasks`
  ADD PRIMARY KEY (`task_id`),
  ADD KEY `request_id` (`request_id`),
  ADD KEY `household_id` (`household_id`),
  ADD KEY `worker_id` (`worker_id`);

--
-- Indexes for table `households`
--
ALTER TABLE `households`
  ADD PRIMARY KEY (`household_id`);

--
-- Indexes for table `notifications`
--
ALTER TABLE `notifications`
  ADD PRIMARY KEY (`notif_id`),
  ADD KEY `user_id` (`receiver_id`);

--
-- Indexes for table `requestrecipients`
--
ALTER TABLE `requestrecipients`
  ADD PRIMARY KEY (`recipient_id`),
  ADD KEY `request_id` (`request_id`),
  ADD KEY `worker_id` (`worker_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indexes for table `workers`
--
ALTER TABLE `workers`
  ADD PRIMARY KEY (`worker_id`);

--
-- Indexes for table `workrequests`
--
ALTER TABLE `workrequests`
  ADD PRIMARY KEY (`request_id`),
  ADD KEY `workrequests_ibfk_1` (`household_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `assignedtasks`
--
ALTER TABLE `assignedtasks`
  MODIFY `task_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=32;

--
-- AUTO_INCREMENT for table `notifications`
--
ALTER TABLE `notifications`
  MODIFY `notif_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT for table `requestrecipients`
--
ALTER TABLE `requestrecipients`
  MODIFY `recipient_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=121;

--
-- AUTO_INCREMENT for table `workrequests`
--
ALTER TABLE `workrequests`
  MODIFY `request_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=44;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `assignedtasks`
--
ALTER TABLE `assignedtasks`
  ADD CONSTRAINT `assignedtasks_ibfk_1` FOREIGN KEY (`request_id`) REFERENCES `workrequests` (`request_id`),
  ADD CONSTRAINT `assignedtasks_ibfk_2` FOREIGN KEY (`household_id`) REFERENCES `households` (`household_id`),
  ADD CONSTRAINT `assignedtasks_ibfk_3` FOREIGN KEY (`worker_id`) REFERENCES `workers` (`worker_id`);

--
-- Constraints for table `households`
--
ALTER TABLE `households`
  ADD CONSTRAINT `households_ibfk_user` FOREIGN KEY (`household_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `notifications`
--
ALTER TABLE `notifications`
  ADD CONSTRAINT `notifications_ibfk_1` FOREIGN KEY (`receiver_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `requestrecipients`
--
ALTER TABLE `requestrecipients`
  ADD CONSTRAINT `requestrecipients_ibfk_1` FOREIGN KEY (`request_id`) REFERENCES `workrequests` (`request_id`),
  ADD CONSTRAINT `requestrecipients_ibfk_2` FOREIGN KEY (`worker_id`) REFERENCES `workers` (`worker_id`);

--
-- Constraints for table `workers`
--
ALTER TABLE `workers`
  ADD CONSTRAINT `workers_ibfk_user` FOREIGN KEY (`worker_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `workrequests`
--
ALTER TABLE `workrequests`
  ADD CONSTRAINT `workrequests_ibfk_1` FOREIGN KEY (`household_id`) REFERENCES `households` (`household_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
