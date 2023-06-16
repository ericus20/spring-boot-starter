#### Stage 1: Build the application
FROM amazoncorretto:17 AS BUILD_IMAGE

# Set the current working directory inside the image
WORKDIR /app

# Copy gradle executable to the image
COPY gradlew .
COPY gradle gradle
COPY build.gradle .

# Set permission to execute file
RUN chmod +x gradlew

# Prepare and install dos2unix to make gradlew file accessible
RUN yum clean all && \
    yum -y update && \
    yum -y install dos2unix

#RUN yum update
#RUN yum install dos2unix
RUN dos2unix gradlew

# Copy the project source
COPY src src
COPY libs/newrelic newrelic

COPY src/main/scripts/wait-for-it.sh wait-for-it.sh
RUN chmod +x wait-for-it.sh && dos2unix wait-for-it.sh

COPY src/main/scripts/start.sh start.sh
RUN chmod +x start.sh && dos2unix start.sh

# Package the application
RUN ./gradlew bootJar

WORKDIR /app/build
RUN mkdir -p dependency  \
    && (cd dependency || return; jar -xf ../libs/*.jar)

#### Stage 2: A minimal docker image with command to run the app
FROM --platform=linux/amd64 amazoncorretto:17 AS RUNNER

# Set the current working directory inside the image
WORKDIR /app

ARG DEPENDENCY=/app/build/dependency

# Copy project dependencies from the build stage
COPY --from=BUILD_IMAGE ${DEPENDENCY}/BOOT-INF/lib ./lib
COPY --from=BUILD_IMAGE ${DEPENDENCY}/META-INF ./META-INF
COPY --from=BUILD_IMAGE ${DEPENDENCY}/BOOT-INF/classes .
COPY --from=BUILD_IMAGE /app/newrelic ./newrelic

COPY --from=BUILD_IMAGE /app/wait-for-it.sh ./wait-for-it.sh
COPY --from=BUILD_IMAGE /app/start.sh ./start.sh

EXPOSE 8080

ENTRYPOINT ["./start.sh"]
