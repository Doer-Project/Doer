package model.household;

import app.household.OngoingWorkService;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import model.SessionManager;
import javafx.scene.control.Button;
import util.MessageBox;

public class OngoingWork {
    private String taskName;
    private String description;
    private String date;
    private String status;
    private int request_id;

    private String workerId = SessionManager.getUserID();

    private String workerName;
    private String startTime;
    private String endTime;
    private String expectedCost;
    private ObjectProperty<Button> selectButton;

    OngoingWorkService service = new OngoingWorkService();

    public OngoingWork(String taskName, String description, String date, int request_id) {
        this.taskName = taskName;
        this.description = description;
        this.date = date;
        this.request_id = request_id;
    }

    public OngoingWork(String requst_id, String workerId, String workerName, String status, String startTime, String endTime, String expectedCost) {
        this.request_id = Integer.parseInt(requst_id);
        this.status = status;
        this.workerId = workerId;
        this.workerName = workerName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.expectedCost = expectedCost;


        /// for only worker who are intrested to work.
        if ("interested".equalsIgnoreCase(status)) {
            Button select = new Button("Hire");
            select.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
            this.selectButton = new SimpleObjectProperty<>(select);
        } else {
            // Safe placeholder instead of null
            this.selectButton = new SimpleObjectProperty<>(new Button("Not Interested"));
            this.selectButton.get().setDisable(true); // Make it non-clickable
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

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getExpectedCost() {
        return expectedCost;
    }

    public int getRequest_id() {
        return request_id;
    }

    public ObjectProperty<Button> getSelectButton() {
        return selectButton;
    }

}
