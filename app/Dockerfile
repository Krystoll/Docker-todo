#  STAGE 1: сборка
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

# Копируем исходники и компилируем
COPY src ./src
RUN mvn clean package -DskipTests -Dcheckstyle.skip=true

#  STAGE 2: запуск
FROM eclipse-temurin:21-jre

WORKDIR /app

# Создаём непривилегированного пользователя
RUN addgroup --system appgroup && adduser --system --ingroup appgroup appuser

COPY --from=builder /app/target/*.jar app.jar

# Меняем владельца файла
RUN chown appuser:appgroup app.jar

USER appuser

# Документируем порт
EXPOSE 8080

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-jar", "app.jar"]