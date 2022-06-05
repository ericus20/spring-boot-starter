## All tests are written in JUnit 5.

## Equals, Hashcode and ToString

- Equals and Hashcode is tested using [EqualsVerifier](https://github.com/jqno/equalsverifier)
- Sample code is

```java
class UserTest {

    @Test
    void equalsContract() {
        User client = UserUtils.createUser();
        User admin = UserUtils.createUser();

        EqualsVerifier.forClass(User.class)
                .withRedefinedSuperclass()
                .withPrefabValues(User.class, client, admin)
                .withOnlyTheseFields(TestUtils.getUserEqualsFields().toArray(new String[0]))
                .verify();
    }
}
```

- ToString is tested using [To String Verifier](https://github.com/jparams/to-string-verifier)
- Sample code is

```java
class UserTest {

    @Test
    void testToString() {
        ToStringVerifier.forClass(User.class)
                .withClassName(NameStyle.SIMPLE_NAME)
                .withIgnoredFields("password", "userRoles", "userHistories")
                .verify();
    }
}
```

## Local Amazons3 Integration Tests

Tests are powered by [S3 mock library for Java/Scala](https://github.com/findify/s3mock)

Bean definition for Amazons3 in test context is defined as:

```java

@Configuration
@Profile(ProfileTypeConstants.TEST)
public class TestConfig {

    /**
     * A bean to be used by AmazonS3 Service.
     *
     * @param props the aws properties
     * @return instance of AmazonS3Client
     */
    @Bean
    public AmazonS3 amazonS3(AwsProperties props) {
        var endpoint = new EndpointConfiguration(props.getServiceEndpoint(), props.getRegion());
        // Create the credentials provider
        var credentials = new AnonymousAWSCredentials();

        return AmazonS3ClientBuilder.standard()
                .withPathStyleAccessEnabled(true)
                .withEndpointConfiguration(endpoint)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }
}

```
