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
FROM openjdk:17-jdk-slim

WORKDIR /app

# 보안 업데이트
RUN apt-get update && apt-get upgrade -y && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# 보안을 위한 전용 사용자 생성
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring

# JAR 파일과 매퍼 파일 복사 시 권한 설정
COPY --from=builder --chown=spring:spring /app/build/libs/*.jar app.jar
COPY --from=builder --chown=spring:spring /app/src/main/resources/mappers/ /app/mappers/

# 파일 권한 수정
USER root
RUN chmod 644 /app/mappers/**/*.xml
USER spring:spring

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=prod

# 매퍼 위치를 명시적으로 지정
ENV MYBATIS_MAPPER_LOCATIONS=file:/app/mappers/**/*.xml

# 매퍼 파일 복사 로깅 추가
RUN ls -la /app/mappers/admin/store/

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-Dlogging.level.org.mybatis=DEBUG", "-jar", "app.jar"]