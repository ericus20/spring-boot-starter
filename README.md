<!--
*** It takes a beautiful community with great minds to create a useful template.
*** Please help us to make it better by contributing to this project.
*** We welcome all suggestions through forks or issue.
*** Happy Coding!
-->

# Spring Boot Starter

A highly opinionated and complete starter for Spring Boot production ready projects.

## Running Instance on Heroku

https://spring-boot-starter.herokuapp.com/

![Java CI with Gradle](https://github.com/ericus20/spring-boot-starter/workflows/Java%20CI%20with%20Gradle/badge.svg)
![pre-commit](https://img.shields.io/badge/pre--commit-enabled-brightgreen?logo=pre-commit&logoColor=white)

## Built With

* [Spring Boot 2.7.1](https://projects.spring.io/spring-boot/) - Inversion of Control Framework
* [Bootstrap 5](https://getbootstrap.com/docs/5.0/getting-started/introduction/) - HTML, CSS, and
  JavaScript framework
* [Thymeleaf](https://www.thymeleaf.org/) - Modern server-side Java template engine
* [Gradle](https://gradle.org/) - Dependency Management and Build Tool
* [Lombok](https://projectlombok.org/) - Automatically plugs into your editor and build tools,
  spicing up your java.
* [AWS](https://aws.amazon.com/) - On-demand cloud computing platforms
* [H2](http://www.h2database.com/) - In-Memory Database for development
* [Liquibase](https://liquibase.org/) - Rapidly manage database schema changes.
* [Spotless](https://github.com/diffplug/spotless/) - Spotless is a general-purpose formatting
  plugin.
* [Hibernate Envers](https://hibernate.org/orm/envers/) - Detailed Auditing of CRUD operations.
* [Spring Security](https://docs.spring.io/spring-boot/docs/2.6.7/reference/htmlsingle/#boot-features-security)
* [Java Mail Sender](https://docs.spring.io/spring-boot/docs/2.6.7/reference/htmlsingle/#boot-features-email)
* [Spring Configuration Processor](https://docs.spring.io/spring-boot/docs/2.6.7/reference/htmlsingle/#configuration-metadata-annotation-processor)
* [Rest Repositories](https://docs.spring.io/spring-boot/docs/2.6.7/reference/htmlsingle/#howto-use-exposing-spring-data-repositories-rest-endpoint)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/2.6.7/reference/htmlsingle/#production-ready)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.6.7/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [Validation](https://docs.spring.io/spring-boot/docs/2.6.7/reference/htmlsingle/#boot-features-validation)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/2.6.7/reference/htmlsingle/#using-boot-devtools)
* [Spring Data Jpa DataTables](https://github.com/darrachequesne/spring-data-jpa-datatables)

## Features

- [DataTables with Spring Data JPA](documents/datatables.md)
- [OpenApi3 Swagger](documents/SWAGGER.md)
- [Form Validation](https://formvalidation.io/)
- [Last Successful Login](documents/AUTHENTICATION.MD)
- [New Relic Integration](documents/NEWRELIC.MD)
- [Bruteforce Attack Prevention using Failed Login Attempts](documents/AUTHENTICATION.MD)
- [Unit and Integration Tests](documents/TESTS.md)
- [API Login Controller](documents/API_LOGIN_CONTROLLER.MD)
- [Advanced CORS Configuration](documents/advanced-cors-configuration.md)
- [Fully Covered Equals, HashCode, ToString Testing](documents/TESTS.md#equals-hashcode-and-tostring)
- [Amazon S3 Implementation](documents/S3.md#amazon-s3-implementation)
- [Local AmazonS3 Integration Tests with S3Mock](documents/TESTS.md#local-amazons3-integration-tests)
- [Production Ready Folder Structure](README.md#production-ready-folder-structure)
- [Authentication and Authorization with JWT](documents/AUTHENTICATION.MD)
- [Email Service with HTML and attachment support](documents/EMAIL_SERVICE.md)
- [Security Configuration Without WebSecurityConfigurerAdapter](https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter#ldap-authentication)

## Sample *ongoing* project using this template

- Used as the API module along with NextJS and AngularJS
  for <a href="https://github.com/stevartz/upsidle" target="_blank">Upsidle: An E-Commerce
  Application</a>

## Entity Relational Diagram (ERD)

![img.png](documents/images/erd.png)

## Production Ready Folder Structure

```bash
.
|-- documents
|-- gradle
|   `-- wrapper
`-- src
    |-- integration
    |   |-- java
    |   |   `-- com
    |   |       `-- developersboard
    |   |           |-- backend
    |   |           `-- web
    |   `-- resources
    |-- main
    |   |-- java
    |   |   `-- com
    |   |       `-- developersboard
    |   |           |-- annotation  # All custom annotations used in the application
    |   |           |-- backend     # Business Logic and Data Access implementation
    |   |           |-- config      # Configuration classes and properties
    |   |           |-- constant    # Constants used in the application
    |   |           |-- enums       # Enums used in the application
    |   |           |-- exception   # Custom exceptions used in the application
    |   |           |-- shared      # Resources like dto, utils, etc. used in the application
    |   |           `-- web         # Web layer implementation
    |   `-- resources
    |       `-- i18n                # I18n property files, comes with 'en', 'fr', 'es' and 'zn_CN'
    `-- test
        |-- java
        |   `-- com
        |       `-- developersboard
        |           |-- backend
        |           `-- web
        `-- resources

```

Configuration properties required to deploy to Heroku:

![img.png](documents/images/heroku-deployment-properties.png)

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426)
for details on our code of conduct, and the process for submitting pull requests to us.

## Credit

TBD

## Authors

* **Eric Opoku** - *Initial work*
* **George Anguah** - *Initial work*
* **Matthew Puentes** - *Initial work*
* **Stephen Boakye** - *Contributor*
* **Charles Dimbeng** - *Contributor*
* **Simon Kodua** - *Contributor*

## Acknowledgments

* To Support
* To learn
* etc

## Notes

* The following environment variables can be customized as necessary.:

- The defaults are:

        ADMIN_USERNAME=admin
        ADMIN_PASSWORD=password
        ENCRYPTION_SECRET_SALT=salt
        ENCRYPTION_SECRET_PASSWORD=password
        JWT_SECRET=salt
        SPRING_PROFILES_ACTIVE=dev

- The AWS Properties and defaults are:

        AWS_REGION=us-east-1
        AWS_ACCESS_KEY_ID=AKIAIOSFODNN7EXAMPLE
        AWS_SECRET_ACCESS_KEY=wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY
        AWS_S3_BUCKET_NAME=spring-boot-starter

- The Email Properties and defaults are:

        EMAIL_PROTOCOL=smtp
        EMAIL_HOST=smtp.gmail.com
        EMAIL_PORT=587
        EMAIL_USERNAME
        EMAIL_PASSWORD # If using gmail, this must be a 2 step verification enabled app password

* Default profile for the application is **dev**, **test** and **prod** to test out production
  functionalities.
    - Datasource must be provided for production profile for the application to run.

*NB: Windows users must use **gradlew** instead of **./gradlew***

* Start Spring Boot application using on linux/unix - **./gradlew bootRun**
* Run unit tests using - **./gradlew test**
* Run integration tests using - **./gradlew integrationTest**
* Run all tests using - **./gradlew testAll**
*
    * Access application on *http://localhost:8080/*
* Access in-memory database on *http://localhost:8080/console*
* Run owasp dependency check - **./gradlew dependencyCheckAnalyze --info**
* Access Swagger UI - **http://localhost:8080/swagger-ui/index.html**

## Running on Docker (Assuming docker is installed)

* in the directory where docker-compose.yml file resides, simply run the command - **docker-compose
  up**

## Stop running application on Docker

* in the directory where docker-compose.yml file resides, simply run the command - **docker-compose
  down**
