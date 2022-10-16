package model;

public class Employee {
    private String id;
    private String type;
    private String name;
    private String address;
    private String contact;
    private String gender;
    private String birthDay;
    private String joinDate;
    private double salary;
    private String email;

    public Employee() {
    }

    public Employee(String id, String type, String name, String address, String contact, String gender, String birthDay, String joinDate, double salary, String email) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.gender = gender;
        this.birthDay = birthDay;
        this.joinDate = joinDate;
        this.salary = salary;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", contact='" + contact + '\'' +
                ", gender='" + gender + '\'' +
                ", birthDay='" + birthDay + '\'' +
                ", joinDate='" + joinDate + '\'' +
                ", salary=" + salary +
                ", email='" + email + '\'' +
                '}';
    }
}
