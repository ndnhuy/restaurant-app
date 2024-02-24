# restaurant-app
Restaurant saas toy

- Run the app
```
mvn spring-boot:run
```

- Run test
```
./mvnw test 
./mvnw test -Dtest="RestaurantControllerTest#testCreateRestaurant"
```

- Migrate flyway with maven plugin
```
mvn clean flyway:migrate -Dflyway.configFiles=flyway.conf
```