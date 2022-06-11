#### Stage 1: Build the application
FROM openjdk:17-alpine as build

# Set the current working directory inside the image
WORKDIR /app

# Copy gradle executable to the image
COPY gradlew .
COPY gradle gradle
COPY build.gradle .

# Copy the libs folder
COPY libs libs

# Set permission to execute file
RUN chmod +x gradlew

# Prepare and install dos2unix to make gradlew file accessible
RUN apk update && apk add dos2unix
RUN dos2unix gradlew

RUN apk update
RUN apk add curl

# Copy the project source
COPY src src

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
FROM openjdk:17-jdk-slim

# Set the current working directory inside the image
WORKDIR /app

ARG DEPENDENCY=/app/build/dependency

# Copy project dependencies from the build stage
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib ./lib
COPY --from=build ${DEPENDENCY}/META-INF ./META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes .

COPY --from=build /app/wait-for-it.sh ./wait-for-it.sh
COPY --from=build /app/start.sh ./start.sh

ENTRYPOINT ["./start.sh"]
