FROM maven:3.8-jdk-11 As builder
WORKDIR /employee-service
COPY . .
RUN mvn package -DskipTests



FROM openjdk:11
WORKDIR /employee-svc
COPY --from=builder /employee-service/target/employee-service-0.0.1-SNAPSHOT.jar  .
EXPOSE 9090
ENTRYPOINT ["java","-jar"]
CMD ["employee-service-0.0.1-SNAPSHOT.jar"]