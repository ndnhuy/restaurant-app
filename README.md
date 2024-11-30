# restaurant-app
Restaurant saas toy

- Run app with docker-compose
```
docker stop ra-service
docker rm ra-service
docker-compose up -d --build
```

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

- Docker build
```
eval $(minikube -p minikube docker-env)
docker build -t ra-service-k8s:1.0 -f Dockerfile_k8s .
```

- api
```
curl -H 'Content-Type: application/json' \
    -d '{"customerId": 1, "lineItems":[{"menuItemId": 1, "quantity": 1}]}' \
    -X POST \
    http://localhost:8989/orders
    
curl -H 'Content-Type: application/json' \
    -X POST \
    http://localhost:8989/orders/create/INIT

```
- vegeta
```
echo "POST http://localhost:8989/orders/create/INIT" | vegeta attack -rate=5 -duration=3s | tee results.bin | vegeta report

```

## Test Containers
- enable testcontainer reuse by adding `app.testcontainers.enabled=true` to ~/.testcontainers.properties