FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:resolve

COPY . .

RUN mvn clean package -Dmaven.test.skip=true

COPY target/registry-0.0.1-SNAPSHOT.jar /app/

ENV JAVA_OPTS="-Xmx512m -Xms256m"

CMD ["java", "-jar", "registry-0.0.1-SNAPSHOT.jar"]
