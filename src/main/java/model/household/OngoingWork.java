package model.household;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import model.SessionManager;
import javafx.scene.control.Button;


import java.awt.*;

public class OngoingWork {
    private String taskName;
    private String description;
    private String date;
    private String status;

    private String workerId = SessionManager.getUserID();
    private String workerName;
    private String rating;
    private String startTime;
    private String endTime;
    private String expectedCost;

    private ObjectProperty<Button> selectButton;

    public OngoingWork(String taskName, String description, String date, String status) {
        this.taskName = taskName;
        this.description = description;
        this.date = date;
        this.status = status;
    }

    public OngoingWork(String workerId, String workerName, String status, String startTime, String endTime, String expectedCost) {
        this.status = status;
        this.workerId = workerId;
        this.workerName = workerName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.expectedCost = expectedCost;


        /// for only worker who are intrested to work.
        if ("interested".equalsIgnoreCase(status)) {
            Button select = new Button("Select");

            select.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
            select.setOnAction(e -> {
                System.out.println("✅ Worker selected: " + workerName);
                // TODO: After selecting this button, we have to call the next logic of the Database
            });
            this.selectButton = new SimpleObjectProperty<>(select);
        } else {
            this.selectButton = new SimpleObjectProperty<>(null);
        }
    }

    public String getTaskName() {
        return taskName;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public String getWorkerId() {
        return workerId;
    }

    public String getWorkerName() {
        return workerName;
    }

    public String getRating() {
        return rating;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getExpectedCost() {
        return expectedCost;
    }
}
