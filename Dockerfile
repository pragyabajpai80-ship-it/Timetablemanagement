FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

RUN mkdir -p bin && \
    javac --add-modules jdk.httpserver \
    -cp "lib/mariadb-java-client.jar" \
    -d bin \
    src/DatabaseConnection.java src/TimetableWebServer.java

EXPOSE 8080

CMD ["sh", "-c", "java --add-modules jdk.httpserver -cp 'bin:lib/mariadb-java-client.jar' TimetableWebServer"]
