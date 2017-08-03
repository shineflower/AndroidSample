package com.jackie.sample.data_binding;

/**
 * Created by Administrator on 2016/10/26.
 */

public class Employee {
    private String firstName;
    private String lastName;
    private boolean isFired;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isFired() {
        return isFired;
    }

    public void setFired(boolean fired) {
        isFired = fired;
    }

    public Employee() {
    }

    public Employee(String firstName, String lastName, boolean isFired) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.isFired = isFired;
    }
}
