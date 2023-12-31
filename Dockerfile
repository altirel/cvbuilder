FROM bellsoft/liberica-openjdk-alpine:17
VOLUME /tmp
COPY target/cvbuilder-*.jar app.jar
EXPOSE 8080
ENV DATABASE_USERNAME=root
ENV DATABASE_PASSWORD=root
ENV DATABASE_HOST=mongodb
ENV DATABASE_PORT=27017
ENV DATABASE_NAME=cvbuilder
ENTRYPOINT ["java", "-jar", "/app.jar"]