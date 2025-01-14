//package com.ohgiraffers.ukki.reservation.model.repository;
//
//import com.ohgiraffers.ukki.reservation.model.entity.ReservationEntity;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
//    List<ReservationEntity> findAllByExpirationDateBefore(LocalDateTime now);
//}
//
