
## All tests are written in JUnit 5.

## Equals, Hashcode and ToString

- Equals and Hashcode is tested using [EqualsVerifier](https://github.com/jqno/equalsverifier)
- Sample code is

    ```
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
    ```

- ToString is tested using [To String Verifier](https://github.com/jparams/to-string-verifier)
- Sample code is

    ```
  @Test
  void testToString() {

      ToStringVerifier.forClass(User.class)
          .withClassName(NameStyle.SIMPLE_NAME)
          .withIgnoredFields("password", "userRoles", "userHistories")
          .verify();
  }
    ```
