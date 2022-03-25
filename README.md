## Spring Boot Starter

A highly opinionated and complete starter for Spring Boot projects ready to production

## Built With

* [Spring Boot](https://projects.spring.io/spring-boot/) - Inversion of Control Framework
* [Bootstrap 5](https://getbootstrap.com/docs/5.0/getting-started/introduction/) - HTML, CSS, and
  JavaScript framework
* [Thymeleaf](https://www.thymeleaf.org/) - Modern server-side Java template engine
* [Gradle](https://gradle.org/) - Dependency Management and Build Tool
* [Lombok](https://projectlombok.org/) - Automatically plugs into your editor and build tools,
  spicing up your java.
* [NextJS](https://nextjs.org/) - The React Framework for production
* [MySQL](https://www.mysql.com/) - Open-source relational database management system
* [AWS](https://aws.amazon.com/) - On-demand cloud computing platforms
* [H2](http://www.h2database.com/) - In-Memory Database for development
* [Spotless](https://github.com/diffplug/spotless/) - Spotless is a general-purpose formatting
  plugin.
* [Spring Security](https://docs.spring.io/spring-boot/docs/2.6.5/reference/htmlsingle/#boot-features-security)
* [Java Mail Sender](https://docs.spring.io/spring-boot/docs/2.6.5/reference/htmlsingle/#boot-features-email)
* [Spring Configuration Processor](https://docs.spring.io/spring-boot/docs/2.6.5/reference/htmlsingle/#configuration-metadata-annotation-processor)
* [Rest Repositories](https://docs.spring.io/spring-boot/docs/2.6.5/reference/htmlsingle/#howto-use-exposing-spring-data-repositories-rest-endpoint)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/2.6.5/reference/htmlsingle/#production-ready)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.6.5/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [Validation](https://docs.spring.io/spring-boot/docs/2.6.5/reference/htmlsingle/#boot-features-validation)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/2.6.5/reference/htmlsingle/#using-boot-devtools)

## Running Instance on AWS

TBD

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

* Default profile for the application is **dev** and **prod** to test out production
  functionalities.
    - Datasource must be provided for production profile for the application to run.


* Start Spring Boot application using - **./gradlew bootRun**
* Access in-memory database on *http://localhost:8080/console*
