package com.paveways.Feedback;

public class Staff_Model {

    String id, department,name;

    public Staff_Model(String id, String department, String  name) {
        this.id = id;
        this.department = department;
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return department; // Display the department name in the spinner
    }
}
