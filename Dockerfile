FROM bellsoft/liberica-runtime-container:jdk-17-glibc
VOLUME /tmp
COPY build/libs/people_backend-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "/app.jar"]
# ENTRYPOINT ["java", "-jar", "/app.jar"]