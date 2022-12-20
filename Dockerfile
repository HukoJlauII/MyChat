#FROM gradle:jdk17 AS gradle_build
#COPY --chown=gradle:gradle . /home/gradle
#RUN gradle build || return 1
#
#FROM amazoncorretto:17
#COPY --from=gradle_build /home/gradle/build/libs/MyChat-0.0.1-SNAPSHOT.jar MyChat-0.0.1-SNAPSHOT.jar
#ENTRYPOINT ["java","-jar","/MyChat-0.0.1-SNAPSHOT.jar"]

FROM gradle AS TEMP_BUILD_IMAGE
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY build.gradle settings.gradle $APP_HOME

COPY gradle $APP_HOME/gradle
COPY --chown=gradle:gradle . /home/gradle/src
USER root
RUN chown -R gradle /home/gradle/src

RUN gradle build || return 0
COPY . .
RUN gradle clean build

FROM amazoncorretto:17
ENV ARTIFACT_NAME=MyChat-0.0.1-SNAPSHOT.jar
ENV APP_HOME=/usr/app/

WORKDIR $APP_HOME
COPY --from=TEMP_BUILD_IMAGE $APP_HOME/build/libs/$ARTIFACT_NAME .

EXPOSE 8080
ENTRYPOINT exec java -jar ${ARTIFACT_NAME}