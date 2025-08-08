package model;

public class PastWork {
    private int workId;
    private String serviceName;
    private String workerName;
    private String dateCompleted;
    private int rating;
    private String review;
    private double amountPaid;

    public PastWork(int workId, String serviceName, String workerName, String dateCompleted,
                    int rating, String review, double amountPaid) {
        this.workId = workId;
        this.serviceName = serviceName;
        this.workerName = workerName;
        this.dateCompleted = dateCompleted;
        this.rating = rating;
        this.review = review;
        this.amountPaid = amountPaid;
    }

    // Getters
    public int getWorkId() {
        return workId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getWorkerName() {
        return workerName;
    }

    public String getDateCompleted() {
        return dateCompleted;
    }

    public int getRating() {
        return rating;
    }

    public String getReview() {
        return review;
    }

    public double getAmountPaid() {
        return amountPaid;
    }
}
