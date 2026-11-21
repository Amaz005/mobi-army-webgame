# Build stage: compile the server and collect its runtime dependencies.
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn -B -q dependency:go-offline
COPY src ./src
RUN mvn -B -q package -DskipTests dependency:copy-dependencies -DoutputDirectory=target/lib

# Runtime stage: JRE only. Paths below are relative to /app, as the server expects.
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /build/target/classes ./classes
COPY --from=build /build/target/lib ./lib
COPY res ./res
COPY data ./data
# Default config; docker-compose bind-mounts the repo copy over it (edit + restart, no rebuild).
COPY army2.conf ./army2.conf
EXPOSE 19149
ENTRYPOINT ["java", "-Dfile.encoding=UTF-8", "-cp", "classes:lib/*", "com.mobiarmy.server.GameServer"]
