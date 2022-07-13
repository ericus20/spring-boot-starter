# Datatables With Spring Data-JPa

- Server side rendering with spring data JPA making use of DTO projections.

## Configuration begins with joining JPA repositories with the DataTables repository interface.

```java
package com.developersboard.config.jpa;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
        repositoryFactoryBeanClass = DataTablesRepositoryFactoryBean.class,
        basePackages = "com.developersboard.backend.persistent.repository")
@EntityScan(basePackages = "com.developersboard.backend.persistent.domain")
public class JpaConfig {

}
```


## Then the definition of the DataTables repository interface.

```java
package com.developersboard.backend.persistent.repository;

import com.developersboard.backend.persistent.domain.user.User;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository
    extends DataTablesRepository<User, Long>, JpaRepository<User, Long> {
}
```

## Then the logic to retrieve the data from the database.

```java
package com.developersboard.backend.service.user;

public interface UserService {

  DataTablesOutput<UserResponse> getUsers(final DataTablesInput dataTablesInput);
}
```

## Finally, the scripting to consume the controller.

```bash
resources/templates/user/index.html
```
