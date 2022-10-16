package model;

public class Repair {
    private String id;
    private String customerId;
    private String description;
    private String date;
    private String time;
    private double price;
    private String status;

    public Repair() {
    }

    public Repair(String id, String customerId, String description, String date, String time, double price, String status) {
        this.id = id;
        this.customerId = customerId;
        this.description = description;
        this.date = date;
        this.time = time;
        this.price = price;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Repair{" +
                "id='" + id + '\'' +
                ", customerId='" + customerId + '\'' +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", price=" + price +
                ", status='" + status + '\'' +
                '}';
    }
}
