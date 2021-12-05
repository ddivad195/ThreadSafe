FROM gradle:7.1-jdk16 AS build
COPY --chown=gradle:gradle . /threadsafe
WORKDIR /threadsafe
RUN gradle shadowJar --no-daemon

FROM openjdk:11.0.8-jre-slim
RUN mkdir /config/
COPY --from=build /threadsafe/build/libs/*.jar /

ENTRYPOINT ["java", "-jar", "/Threadsafe.jar"]