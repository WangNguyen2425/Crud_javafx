package Java.model;

import java.sql.Date;

public class Teacher {
    private Integer teacherId;
    private String fullName;
    private String position;
    private String classSupervisor;
    private String teacherSchedule;
    private String sex;
    private String address;
    private String contractStartDate;
    private String contractEndDate;
    private Integer salary;

    public Teacher(Integer teacherId, String fullName, String position, String classSupervisor, String teacherSchedule, String sex, String address, String contractStartDate, String contractEndDate, Integer salary) {
        this.teacherId = teacherId;
        this.fullName = fullName;
        this.position = position;
        this.classSupervisor = classSupervisor;
        this.teacherSchedule = teacherSchedule;
        this.sex = sex;
        this.address = address;
        this.contractStartDate = contractStartDate;
        this.contractEndDate = contractEndDate;
        this.salary = salary;
    }

    public Teacher() {
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getClassSupervisor() {
        return classSupervisor;
    }

    public void setClassSupervisor(String classSupervisor) {
        this.classSupervisor = classSupervisor;
    }

    public String getTeacherSchedule() {
        return teacherSchedule;
    }

    public void setTeacherSchedule(String teacherSchedule) {
        this.teacherSchedule = teacherSchedule;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContractStartDate() {
        return contractStartDate;
    }

    public String getContractEndDate() {
        return contractEndDate;
    }

    public void setContractStartDate(String contractStartDate) {
        this.contractStartDate = contractStartDate;
    }

    public void setContractEndDate(String contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }
}
