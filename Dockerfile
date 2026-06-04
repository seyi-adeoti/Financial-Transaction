FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

COPY .mvn .mvn
COPY mvnw pom.xml ./
COPY src ./src

RUN chmod +x ./mvnw && ./mvnw -B -DskipTests package

EXPOSE 9090

CMD ["java", "-jar", "target/Moneymanager-0.0.1-SNAPSHOT.jar"]
