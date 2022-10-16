package model;

public class Return {
    private String id;
    private String orderId;
    private String customerName;
    private String note;
    private double amount;

    public Return() {
    }

    public Return(String id, String orderId, String customerName, String note, double amount) {
        this.id = id;
        this.orderId = orderId;
        this.customerName = customerName;
        this.note = note;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Return{" +
                "id='" + id + '\'' +
                ", orderId='" + orderId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", note='" + note + '\'' +
                ", amount=" + amount +
                '}';
    }
}
