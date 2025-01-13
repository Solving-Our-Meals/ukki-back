//package com.ohgiraffers.ukki.reservation.model.entity;
//
//import com.ohgiraffers.ukki.reservation.model.entity.ReservationEntity;
//import jakarta.persistence.Entity;
//import jakarta.persistence.Id;
//
//import java.time.LocalDateTime;
//
//@Entity
//public class ExpiredReservationEntity {
//    @Id
//    private Long id;
//    private String name;
//    private LocalDateTime reservationDate;
//    private LocalDateTime expirationDate;
//
//    public ExpiredReservationEntity(){}
//
//    public ExpiredReservationEntity(ReservationEntity reservationEntity){
//        this.id = reservationEntity.getId();
//        this.name = reservationEntity.getName();
//        this.reservationDate = reservationEntity.getReservationDate();
//        this.expirationDate = reservationEntity.getExpirationDate();
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
//        return "ExpiredReservationEntity{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", reservationDate=" + reservationDate +
//                ", expirationDate=" + expirationDate +
//                '}';
//    }
//}
