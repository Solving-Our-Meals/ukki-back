package com.ohgiraffers.ukki.user.model.dto;

public class NoshowUserDTO {

    private String email;
    private int noshow;

    public NoshowUserDTO() {}

    public NoshowUserDTO(String email, int noshow) {
        this.email = email;
        this.noshow = noshow;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getNoshow() {
        return noshow;
    }

    public void setNoshow(int noshow) {
        this.noshow = noshow;
    }

    @Override
    public String toString() {
        return "NoshowUserDTO{" +
                "email='" + email + '\'' +
                ", noshow=" + noshow +
                '}';
    }
}
