# restaurant-app
Restaurant saas toy

- Run the app
```
mvn spring-boot:run
```

- Migrate flyway with maven plugin
```
mvn clean flyway:migrate -Dflyway.configFiles=flyway.conf
```