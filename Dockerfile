# Build stage
FROM gradle:7.6.1-jdk17 AS builder

WORKDIR /app

# 그래들 파일 복사
COPY build.gradle settings.gradle ./
COPY gradle gradle

# 의존성 다운로드
RUN gradle dependencies --no-daemon

# 전체 소스 복사
COPY src src

# credentials.json을 마지막에 복사하여 덮어쓰기
COPY src/main/resources/credentials.json src/main/resources/credentials.json

# 빌드
RUN gradle build --no-daemon -x test

# Production stage
FROM openjdk:17-slim

WORKDIR /app

# 보안 업데이트
RUN apt-get update && apt-get upgrade -y && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# 보안을 위한 전용 사용자 생성
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring

# JAR 파일 복사
COPY --from=builder --chown=spring:spring /app/build/libs/*.jar app.jar

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=prod

ENTRYPOINT ["java", "-jar", "app.jar"]