package com.example.moivememoir.entities;

import java.io.Serializable;
import java.sql.Date;

public class Person implements Serializable {
    private Integer personId;
    private String personName;
    private String personSurname;
    private String personGender;
    private String personDob;
    private String personAddress;
    private String personState;

    public Person(Integer personId, String personName, String personSurname,
                  String personGender, String personDob,
                  String personAddress,
                  String personState,
                  String personPostcode) {
        this.personId = personId;
        this.personName = personName;
        this.personSurname = personSurname;
        this.personGender = personGender;
        this.personDob = personDob;
        this.personAddress = personAddress;
        this.personState = personState;
        this.personPostcode = personPostcode;
    }

    private String personPostcode;

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonSurname() {
        return personSurname;
    }

    public void setPersonSurname(String personSurname) {
        this.personSurname = personSurname;
    }

    public String getPersonGender() {
        return personGender;
    }

    public void setPersonGender(String personGender) {
        this.personGender = personGender;
    }

    public String getPersonDob() {
        return personDob;
    }

    public void setPersonDob(String personDob) {
        this.personDob = personDob;
    }

    public String getPersonAddress() {
        return personAddress;
    }

    public void setPersonAddress(String personAddress) {
        this.personAddress = personAddress;
    }

    public String getPersonState() {
        return personState;
    }

    public void setPersonState(String personState) {
        this.personState = personState;
    }

    public String getPersonPostcode() {
        return personPostcode;
    }

    public void setPersonPostcode(String personPostcode) {
        this.personPostcode = personPostcode;
    }


}
