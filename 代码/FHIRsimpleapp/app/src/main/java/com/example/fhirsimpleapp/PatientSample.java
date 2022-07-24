package com.example.fhirsimpleapp;

import java.io.Serializable;
import java.util.List;

public class PatientSample implements Serializable {
    private String patientid;
    private String gender;
    private String maritualStatus;
    private double Triglycerides;
    private double lowDensity;
    private double highDensity;
    private double age;
    private List<String> practitionersList;
    private String Email;
    private String Phone;
    private String Address;
    private String givenName;
    private String familyName;

    public List<String> getPractitionersList() {
        return practitionersList;
    }

    public void setPractitionersList(List<String> practitionersList) {
        this.practitionersList = practitionersList;
    }

    public String getPatientid() {
        return patientid;
    }

    public void setPatientid(String patientid) {
        this.patientid = patientid;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMaritualStatus() {
        return maritualStatus;
    }

    public void setMaritualStatus(String maritualStatus) {
        this.maritualStatus = maritualStatus;
    }

    public double getTriglycerides() {
        return Triglycerides;
    }

    public void setTriglycerides(double triglycerides) {
        Triglycerides = triglycerides;
    }

    public double getLowDensity() {
        return lowDensity;
    }

    public void setLowDensity(double lowDensity) {
        this.lowDensity = lowDensity;
    }

    public double getHighDensity() {
        return highDensity;
    }

    public void setHighDensity(double highDensity) {
        this.highDensity = highDensity;
    }

    public double getAge() {
        return age;
    }

    public void setAge(double age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "PatientSample{" +
                "patientid=" + patientid +
                ", gender='" + gender + '\'' +
                ", maritualStatus='" + maritualStatus + '\'' +
                ", Triglycerides=" + Triglycerides +
                ", lowDensity=" + lowDensity +
                ", highDensity=" + highDensity +
                ", age=" + age +
                '}';
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }
}
