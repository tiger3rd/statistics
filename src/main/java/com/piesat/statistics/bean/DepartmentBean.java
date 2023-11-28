package com.piesat.statistics.bean;

public class DepartmentBean {
    private String department_id;
    private String department_name;
    private int serial_no;

    private String is_country;

    public String getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    public int getSerial_no() {
        return serial_no;
    }

    public void setSerial_no(int serial_no) {
        this.serial_no = serial_no;
    }

    public String getIs_country() {
        return is_country;
    }

    public void setIs_country(String is_country) {
        this.is_country = is_country;
    }
}
