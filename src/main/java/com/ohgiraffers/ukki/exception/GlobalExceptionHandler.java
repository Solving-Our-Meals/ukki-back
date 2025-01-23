//package com.ohgiraffers.ukki.exception;
//
//import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//
//import java.nio.file.AccessDeniedException;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    //404
//    @ExceptionHandler(ConfigDataResourceNotFoundException.class)
//    public ResponseEntity<String> handleResourceNotFoundException(ConfigDataResourceNotFoundException ex) {
//        return new ResponseEntity<>("리소스를 찾을 수 없습니다. : " + ex.getMessage(), HttpStatus.NOT_FOUND);
//    }
//
//    // 403
//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
//        return new ResponseEntity<>("접근 권한이 없습니다. : " + ex.getMessage(), HttpStatus.FORBIDDEN);
//    }
//
//    // 500
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> handleGenericException(Exception ex) {
//        return new ResponseEntity<>("서버에서 오류가 발생했습니다. : " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//    }
////
////    @ExceptionHandler(Exception.class)
////    public ResponseEntity<String> handleException(Exception ex) {
////        // 로그 기록
////        System.out.println("예외 발생: " + ex.getMessage());
////        ex.printStackTrace();
////
////        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생");
////
////    }
//}