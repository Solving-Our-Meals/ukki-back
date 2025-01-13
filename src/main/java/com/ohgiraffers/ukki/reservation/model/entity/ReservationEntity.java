//package com.ohgiraffers.ukki.reservation.model.entity;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.Id;
//
//import java.time.LocalDateTime;
//
//@Entity
//public class ReservationEntity {
//
//    @Id
//    private Long id;
//    private String name;
//    private LocalDateTime reservationDate;
//    private LocalDateTime expirationDate;
//
//    public ReservationEntity(){}
//
//    public ReservationEntity(Long id, String name, LocalDateTime reservationDate, LocalDateTime expirationDate) {
//        this.id = id;
//        this.name = name;
//        this.reservationDate = reservationDate;
//        this.expirationDate = expirationDate;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public LocalDateTime getReservationDate() {
//        return reservationDate;
//    }
//
//    public void setReservationDate(LocalDateTime reservationDate) {
//        this.reservationDate = reservationDate;
//    }
//
//    public LocalDateTime getExpirationDate() {
//        return expirationDate;
//    }
//
//    public void setExpirationDate(LocalDateTime expirationDate) {
//        this.expirationDate = expirationDate;
//    }
//
//    @Override
//    public String toString() {
//        return "ReservationEntity{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", reservationDate=" + reservationDate +
//                ", expirationDate=" + expirationDate +
//                '}';
//    }
//}
