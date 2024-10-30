package com.ooops.lms.model.datatype;

import com.ooops.lms.model.enums.Gender;

public class Person {
    private String firstName;
    private String lastName;
    private String imagePath;
    private Gender gender;
    private String dateOfBirth;
    private String email;
    private String phone;

    public Person() {

    }

    /**
     * khởi tạo
     * @param firstName tên
     * @param lastName họ
     * @param imagePath avatar
     * @param gender giới tính
     * @param dateOfBirth ngày sinh
     * @param email email
     * @param phone phone number
     */
    public Person(String firstName, String lastName, String imagePath, Gender gender, String dateOfBirth, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.imagePath = imagePath;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public Gender getGender() {
        return gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
