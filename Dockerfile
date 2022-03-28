#### Stage 1: Build the application
FROM openjdk:17-alpine as build

# Set the current working directory inside the image
WORKDIR /app

# Copy gradle executable to the image
COPY gradlew .
COPY gradle gradle
COPY build.gradle .

# Set permission to execute file
RUN chmod +x gradlew

# Copy the project source
COPY src src

# Package the application
RUN ./gradlew bootJar

WORKDIR /app/build
RUN mkdir -p dependency  \
    && (cd dependency || return; jar -xf ../libs/*.jar)

#### Stage 2: A minimal docker image with command to run the app
FROM openjdk:17-alpine

# Set the current working directory inside the image
WORKDIR /app

ARG DEPENDENCY=/app/build/dependency

# Copy project dependencies from the build stage
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib ./lib
COPY --from=build ${DEPENDENCY}/META-INF ./META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes .

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-cp",".:lib/*","com.developersboard.SpringBootStarterApplication"]
