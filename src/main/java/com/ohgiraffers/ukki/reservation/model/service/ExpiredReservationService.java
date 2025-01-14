//package com.ohgiraffers.ukki.reservation.model.service;
//
//import com.ohgiraffers.ukki.reservation.model.entity.ExpiredReservationEntity;
//import com.ohgiraffers.ukki.reservation.model.entity.ReservationEntity;
//import com.ohgiraffers.ukki.reservation.model.repository.ReservationRepository;
//import com.ohgiraffers.ukki.reservation.model.repository.ExpiredReservationRepository;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Service
//public class ExpiredReservationService {
//
//    private final ReservationRepository reservationRepository;
//    private final ExpiredReservationRepository expiredReservationRepository;
//
//    public ExpiredReservationService(ReservationRepository reservationRepository, ExpiredReservationRepository expiredReservationRepository) {
//        this.reservationRepository = reservationRepository;
//        this.expiredReservationRepository = expiredReservationRepository;
//    }
//
//    @Scheduled(cron = "0 0 0 * * ?")
//    public void moveExpiredReservations() {
//        List<ReservationEntity> expiredReservations = reservationRepository.findAllByExpirationDateBefore(LocalDateTime.now());
//        expiredReservations.forEach(reservation -> {
//            expiredReservationRepository.save(new ExpiredReservationEntity(reservation));
//            reservationRepository.delete(reservation);
//        });
//    }

    /*
        MySQL 이벤트와 자바 스케줄러의 역할
       - MySQL 이벤트는 데이터베이스 레벨에서 정해진 주기(ex 하루, 한 시간 등)에 맞춰 만료된 예약을 관리한다.
       - 자바 스케줄러(@Scheduled)는 어플리케이션 레벨에서 주기적인 작업을 수행한다.
       - 이 두 시스템은 독립적으로 동작할 수 있으며, 두 가지 방법 중 하나만 사용해도 동작에는 문제가 없다.
       - 예를 들어, 두 가지 방법을 동시에 사용하는 시나리오를 고려해보면, 데이터 정리 작업을 중복해서 처리할 수도 있으므로 주의해야한다.

       선택과 적용
       1. MySQL 이벤트 사용 :
          이 방법은 데이터베이스 레벨에서 만료된 예약을 관리하는 작업을 자동으로 처리
          별도의 자바 코드 불필요
          MySQL 워크벤치에서 설정한 이벤트가 주기적으로 실행됨
       2. 자바 스케줄러 사용 :
          이 방법은 스프링부트 어플리케이션에서 예약된 작업을 수행하여 만료된 예약을 관리
          이를 위해 스케줄링된 작업을 @Scheduled 어노테이션을 사용하여 설정
    */

//}
