FROM gradle:8.7.0-jdk17

ARG GITHUB_ACTOR
ARG GITHUB_TOKEN

ENV USERNAME ${GITHUB_ACTOR}
ENV TOKEN ${GITHUB_TOKEN}

COPY . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build
EXPOSE 8080
ENTRYPOINT ["java","-jar","/home/gradle/src/build/libs/snippet-runner-0.0.1-SNAPSHOT.jar"]
