FROM eclipse-temurin:24-jdk AS build
WORKDIR /app

ARG DB_HOST
ENV DB_HOST=${DB_HOST}
ARG DB_NAME
ENV DB_NAME=${DB_NAME}
ARG DB_PASS
ENV DB_PASS=${DB_PASS}
ARG DB_PORT
ENV DB_PORT=${DB_PORT}
ARG DB_USER
ENV DB_USER=${DB_USER}

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw -B -DskipTests dependency:go-offline

COPY src ./src
RUN ./mvnw -B -DskipTests package

FROM eclipse-temurin:24-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75"
EXPOSE 8080
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]
