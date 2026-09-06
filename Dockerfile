#### Stage 1: Build the application
FROM amazoncorretto:26 AS build_image

WORKDIR /app

# Install utilities required by Gradle wrapper and shell scripts.
# findutils provides xargs.
RUN yum clean all && \
    yum -y update && \
    yum -y install dos2unix findutils && \
    yum clean all

# Copy Gradle wrapper/configuration first to improve Docker layer caching
COPY gradlew .
COPY gradle gradle
COPY build.gradle .

# Normalize line endings and make Gradle wrapper executable
RUN dos2unix gradlew && \
    chmod +x gradlew

# Copy application source and supporting files
COPY src src
COPY libs/newrelic newrelic

# Prepare startup scripts
COPY src/main/scripts/wait-for-it.sh wait-for-it.sh
COPY src/main/scripts/start.sh start.sh

RUN dos2unix wait-for-it.sh start.sh && \
    chmod +x wait-for-it.sh start.sh

# Build the Spring Boot application
RUN ./gradlew bootJar --no-daemon

# Extract the Spring Boot executable JAR into layers
WORKDIR /app/build

RUN mkdir -p dependency && \
    cd dependency && \
    jar -xf ../libs/*.jar


#### Stage 2: Runtime image
FROM amazoncorretto:26 AS runner

WORKDIR /app

ARG DEPENDENCY=/app/build/dependency

# Copy only what is needed to run the application
COPY --from=build_image ${DEPENDENCY}/BOOT-INF/lib ./lib
COPY --from=build_image ${DEPENDENCY}/META-INF ./META-INF
COPY --from=build_image ${DEPENDENCY}/BOOT-INF/classes ./

COPY --from=build_image /app/newrelic ./newrelic
COPY --from=build_image /app/wait-for-it.sh ./wait-for-it.sh
COPY --from=build_image /app/start.sh ./start.sh

EXPOSE 8080

ENTRYPOINT ["./start.sh"]