# Products
SpringBoot Rest Api tutorial with OpenApi 3.0 and PostgreSql.

## Topics covered
- Spring Boot Rest Api
- Swagger UI for visualizing APIs
- Error Handling
- Basic Authentication
- Mapper for POJO<->DTO
- Actuator
- Eureka server and Open Feign
- Gateway
- Logging
- Testing
    - Repositories using DataJpaTest
    - Services using Mockito
    - EndPoints using WebMvcTest
    - Pojos and Dtos using OpenPojo
    - End to End Test with TestRestTemplate
    - Integration test using TestContainers and Cucumber

## Getting Started
### Compile
    ./gradlew clean build

### Dependency-Check
    ./gradlew dependencyCheckAnalyze --info

### Pitest
    ./gradlew pitest

### SonarQube
Run SonarQube

    docker-compose -f docker/docker-compose-sonar.yml up

 - Sonarqube (http://localhost:9000/)

For SonarQube configuration follow this link: [Try Out SonarQube](https://docs.sonarqube.org/latest/setup/get-started-2-minutes/)

Scan project

    ./gradlew sonarqube \
    -Dsonar.projectKey=yourProjectKey \
    -Dsonar.token=yourAuthenticationToken

### Reports
    company-api/build/reports/checkstyle/main.html
    company-api/build/reports/checkstyle/test.html
    company-api/build/reports/tests/test/index.html
    company-api/build/reports/jacoco/test/index.html
    company-api/build/reports/pitest/index.html
    company-api/build/reports/dependency-check-report.html

    company-dto/build/reports/checkstyle/main.html
    company-dto/build/reports/checkstyle/test.html
    company-dto/build/reports/tests/test/index.html
    company-dto/build/reports/jacoco/test/index.html
    company-dto/build/reports/pitest/index.html
    company-dto/build/reports/dependency-check-report.html

    product-api/build/reports/checkstyle/main.html
    product-api/build/reports/checkstyle/test.html
    product-api/build/reports/tests/test/index.html
    product-api/build/reports/jacoco/test/index.html
    product-api/build/reports/pitest/index.html
    product-api/build/reports/dependency-check-report.html

    product-common/build/reports/checkstyle/main.html
    product-common/build/reports/checkstyle/test.html
    product-common/build/reports/tests/test/index.html
    product-common/build/reports/jacoco/test/index.html
    product-common/build/reports/pitest/index.html
    product-common/build/reports/dependency-check-report.html

    product-dto/build/reports/checkstyle/main.html
    product-dto/build/reports/checkstyle/test.html
    product-dto/build/reports/tests/test/index.html
    product-dto/build/reports/jacoco/test/index.html
    product-dto/build/reports/pitest/index.html
    product-dto/build/reports/dependency-check-report.html

    product-eureka/build/reports/checkstyle/main.html
    product-eureka/build/reports/checkstyle/test.html
    product-eureka/build/reports/tests/test/index.html
    product-eureka/build/reports/jacoco/test/index.html
    product-eureka/build/reports/dependency-check-report.html

    product-gateway/build/reports/checkstyle/main.html
    product-gateway/build/reports/checkstyle/test.html
    product-gateway/build/reports/tests/test/index.html
    product-gateway/build/reports/jacoco/test/index.html
    product-gateway/build/reports/dependency-check-report.html

### Create docker image
    ./gradlew jibDockerBuild

### How to execute
* Applications in java command:

    docker-compose -f docker/docker-compose.yml up
    java -jar ./product-api/build/libs/product-api-1.0-SNAPSHOT.jar
    java -jar ./company-api/build/libs/company-api-1.0-SNAPSHOT.jar

* All applications in docker:

    docker compose -f docker/docker-compose.yml -f docker/docker-product.yml up

* All applications in docker with Prometheus and Grafana:

    docker compose -f docker/docker-compose.yml -f docker/docker-product.yml -f docker/docker-extension.yaml up

**Links**
 - http://localhost:8080 (pgAdmin)
 - http://localhost:8761 (Eureka)
 - http://localhost:3007/swagger-ui.html (Gateway)
 - http://localhost:3080 (Grafana)

### Security
 - Basic Authentication
   - Company Api: (companyUser/companyPassword)
   - Product Api: (productUser/productPassword)
   - Eureka: (eurekaUser/eurekaPassword)
   - pgAdmin: (product@domain-name.com/secret)
   - Gateway: (gatewayUser/gatewayPassword)
   - Grafana: (admin/grafana)

### Actuator
    curl 'http://localhost:8082/actuator/info' -i -X GET
    curl 'http://localhost:8082/actuator/health' -i -X GET
    curl 'http://localhost:8082/actuator/prometheus' -i -X GET
    curl 'http://localhost:8081/actuator/info' -i -X GET
    curl 'http://localhost:8081/actuator/health' -i -X GET
    curl 'http://localhost:8081/actuator/prometheus' -i -X GET

## Integration test

### Publish client and dto jars
    ./gradlew publishToMavenLocal

### Run Integration tests
    cd product-integration-test
    ./gradlew clean build


## Technologies used
- [Gradle 8.5](https://gradle.org)
- [Java 17](https://openjdk.java.net/projects/jdk/17)
- [Spring Boot 3.4](https://spring.io/projects/spring-boot)
- [Spring Security](https://spring.io/projects/spring-security)
- [Swagger OpeApi 3.0](https://swagger.io/specification)
- [Micrometer Prometheus 1.14](https://micrometer.io/docs/registry/prometheus)
- [Mapstruct 1.6](https://mapstruct.org)
- [Lombok 1.18](https://projectlombok.org)
- [Spring Data JPA](https://projects.spring.io/spring-data-jpa)
- [org.postgresql 42.7](https://jdbc.postgresql.org)
- [HsqlDb](http://hsqldb.org)
- [LogBack 1.5](https://logback.qos.ch)
- [Mockito](https://site.mockito.org)
- [JUnit 5](https://junit.org/junit5)
- [OpenPojo 0.9](https://github.com/OpenPojo)
- [CheckStyle 8.44](https://checkstyle.sourceforge.io)
- [Owasp Dependency Check 8.4](https://owasp.org/www-project-dependency-check)
- [Jacoco 0.8](https://www.jacoco.org)
- [Pitest 1.15](https://pitest.org)
- [TestContainers 1.20](https://www.testcontainers.org)
- [Cucumber 7.21](https://cucumber.io)
