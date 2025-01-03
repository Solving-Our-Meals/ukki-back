package com.ohgiraffers.ukki.auth.controller;

import com.ohgiraffers.ukki.auth.model.dto.AuthDTO;
import com.ohgiraffers.ukki.auth.model.dto.ForJwtDTO;
import com.ohgiraffers.ukki.auth.model.service.AuthService;
import com.ohgiraffers.ukki.common.UserRole;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/auth")
@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login/step-one")
    public ResponseEntity<?> checkUserId(@RequestBody AuthDTO authDTO) {
        try {
            int isUserIdValid = authService.isUserIdValid(authDTO.getUserId());
            if (isUserIdValid == 0) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("isValid", false, "message", "ⓘ 아이디가 존재하지 않습니다."));
            }
            return ResponseEntity.ok(Map.of("isValid", true, "message", "ⓘ 아이디 확인 완료"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("isValid", false, "message", "ⓘ 서버 오류가 발생했습니다."));
        }
    }

    @PostMapping("/login/step-two")
    public ResponseEntity<?> authenticatePassword(@RequestBody AuthDTO authDTO, HttpServletResponse response, HttpServletRequest request) {
        ForJwtDTO forJwtDTO = authService.findUserRoleAndUserNoById(authDTO.getUserId());
        UserRole userRole = UserRole.valueOf(forJwtDTO.getUserRole()); // 자료형이 ENUM이어서 String으로 변경해줘야 컴파일 에러가 안남
        try {
            boolean isPasswordValid = authService.authenticateUser(authDTO.getUserId(), authDTO.getUserPass());

            if (!isPasswordValid) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "ⓘ 비밀번호가 잘못되었습니다."));
            }

            String token = authService.createToken(authDTO.getUserId(), userRole, forJwtDTO.getUserNo());

            String existingRefreshToken = authService.getRefresh(request);

            String refreshToken;
            if (existingRefreshToken != null) {
                refreshToken = existingRefreshToken;
            } else {
                refreshToken = authService.createRefreshToken(authDTO.getUserId());
            }

            // 토큰 쿠키 저장용
            Cookie cookie = new Cookie("authToken", token);
            cookie.setHttpOnly(false); // 이것도 배포 전에 false
            cookie.setSecure(false); // HTTPS에서만 전송되게 설정 -> 보안땜시 cookie.setSecure(false);  // 배포전엔 false 사용
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60); // 유효기간 -> 1 시간 -> 24시간 : (60 * 60 * 24)
            response.addCookie(cookie);

            // 리프토 부분
            Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
            refreshCookie.setHttpOnly(false); // 리프레시 토큰은 보안을 위해 HttpOnly 설정
            refreshCookie.setSecure(false); // 배포하면 트루
            refreshCookie.setPath("/");
            refreshCookie.setMaxAge(60 * 60 * 24 * 7); // 7일
            response.addCookie(refreshCookie);

            return ResponseEntity.ok(Map.of("success", true, "message", "ⓘ 로그인 성공 !", "token", token));
        } catch (Exception e) {
            e.printStackTrace(); // 에러가 뭔지 전혀 모르겠으면 사용 -> 사용해보고 에러 보니까 HS512가 512bit수준의 SECRET_KEY를 원하는데 내가 너무 짧게 설정해서 오류가난거임 -> yml에서 해결완료
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "ⓘ 서버 오류가 발생했습니다."));
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                }
            }
        }

        if (refreshToken != null && authService.validateRefreshToken(refreshToken)) {
            /* 결론 : 엑세스 토큰에는 유저 아이디, 역할, 번호가 담기는 상황 -> getUserInfofromToken에서도 마찬가지로 그렇게 로직이 짜임
            리프레시 토큰에서는 유저 아이디만 갖고 있음 -> 이유 : 갱신용이기 때문에 정보가 많이 담길 필요가 없다.
            그러나 리프레시 토큰에서 엑세스 토큰의 3가지 정보를 담는 getUserInfoFormToken을 사용하면서 null이 발생해 문제가 발생했고 토큰이 일치하지 않는다는
            문제가 발생했다. 그래서 리프레시 토큰 전용인 getUserInfoFromRefreshToken 메소드를 만들어서 userId만 담아서 가져오니 정상 작동한다.
            sout가 작동하지 않는게 아니라 위에서 문제가 발생해서 sout까지는 가지도 않는 문제였고 만약 하나의 메소드에서 쓸 경우에는 if 문으로 나눠서할 수 있을 것
            같은데(접근 토큰과 리프레시 토큰의 구분이 가능하다면?) 굳이 그렇게 할 필요없이 이렇게 나누면 될 것 같다.
            개고생했는데 이게 문제였다. 엑세스 토큰의 getUserInfoToken을 그대로 사용할 경우 userRole과 userId를 가져오다보니 null값이 나온다고
            문제가 생겼는데 userId만 넣는게 맞는 것 같다. 로직을 공통으로 쓰기 힘들어지고 로직이 많아진다. 토큰에 담아서 DB에서 찾아다가 쓰는게 맞다.
            팀원의 요청으로 No까지 넣었지만 개인적으로는 하나만 쓰는게 맞지 않나 싶다. -> DB에서 찾아오는 식으로
            다만, 여기서 생기는 의문은 느려질 수 있지 않냐는건데 이건 좀 더 공부해야 될 것 같다.
            */
            Map<String, Object> userInfo = authService.getUserInfoFromRefreshToken(refreshToken);
            String userId = (String) userInfo.get("userId");

            ForJwtDTO forJwtDTO = authService.findUserRoleAndUserNoById(userId);
            UserRole userRole = UserRole.valueOf(forJwtDTO.getUserRole());

            String newToken = authService.createToken(userId, userRole, forJwtDTO.getUserNo());

            Cookie newCookie = new Cookie("authToken", newToken);
            newCookie.setHttpOnly(false);
            newCookie.setSecure(false);
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60); // 1시간
            response.addCookie(newCookie);

            return ResponseEntity.ok(Map.of("token", newToken));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "ⓘ 리프레시 토큰이 유효하지 않습니다."));
        }
    }



    // 로그아웃 관련
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            deleteCookie(request, response, "authToken");
            deleteCookie(request, response, "refreshToken");

            return ResponseEntity.ok(Map.of("success", true, "message", "ⓘ 로그아웃 성공"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "ⓘ 서버 오류가 발생했습니다."));
        }
    }

    private void deleteCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
            }
        }
    }

}
