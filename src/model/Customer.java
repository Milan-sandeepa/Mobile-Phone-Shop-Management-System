package model;

public class Customer {
    private String cId;
    private String name;
    private String address;
    private String contact;
    private String email;

    public Customer() {
    }

    public Customer(String cId, String name, String address, String contact, String email) {
        this.cId = cId;
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.email = email;
    }


    public String getId() {
        return cId;
    }

    public void setId(String cId) {
        this.cId = cId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "cId='" + cId + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", contact='" + contact + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
