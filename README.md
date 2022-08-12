# Product delivery [![tests](https://github.com/daggerok/product-delivery/actions/workflows/tests.yml/badge.svg)](https://github.com/daggerok/product-delivery/actions/workflows/tests.yml)
Event-driven application uses React, reactive Spring Boot WebFlux, R2DBC, MySQL and Liquibase

Status: IN PROGRESS

```bash
if [[ "" != `docker ps -aq` ]] ; then docker rm -f -v `docker ps -aq` ; fi

docker run -d --rm --name mysql --platform=linux/x86_64 \
  --health-cmd='mysqladmin ping -h 127.0.0.1 -u $MYSQL_USER --password=$MYSQL_PASSWORD || exit 1' \
  --health-start-period=1s --health-retries=1111 --health-interval=1s --health-timeout=5s \
  -e MYSQL_ROOT_PASSWORD=app-password -e MYSQL_DATABASE=app-database \
  -e MYSQL_USER=app-user -e MYSQL_PASSWORD=app-password \
  -p 3306:3306 \
  mysql:8.0.30

while [[ $(docker ps -n 1 -q -f health=healthy -f status=running | wc -l) -lt 1 ]] ; do
  sleep 3 ; echo -n '.'
done

sleep 15; echo 'MySQL is ready.'

./mvnw -f app clean compile spring-boot:run

http --ignore-stdin post :8080/api/v1/delivery budget=50 notes='Pick 2 pizzas and deliver to customer'
http --ignore-stdin  get :8080/api/v1/delivery
http --ignore-stdin  put :8080/api/v1/delivery/1/created
http --ignore-stdin  put :8080/api/v1/delivery/1/cancelled

./mvnw -f app spring-boot:stop
docker stop mysql
```

# Links
* [YouTube: Event-Driven Architecture with React and FastAPI – Full Course](https://www.youtube.com/watch?v=NVvIpqmf_Xc&ab_channel=freeCodeCamp.org)


<!--

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.7.2/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.7.2/maven-plugin/reference/html/#build-image)
* [Coroutines section of the Spring Framework Documentation](https://docs.spring.io/spring/docs/5.3.22/spring-framework-reference/languages.html#coroutines)
* [Spring Data R2DBC](https://docs.spring.io/spring-boot/docs/2.7.2/reference/htmlsingle/#data.sql.r2dbc)
* [Spring Reactive Web](https://docs.spring.io/spring-boot/docs/2.7.2/reference/htmlsingle/#web.reactive)
* [Liquibase Migration](https://docs.spring.io/spring-boot/docs/2.7.2/reference/htmlsingle/#howto.data-initialization.migration-tool.liquibase)
* [RSocket](https://rsocket.io/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)
* [Acessing data with R2DBC](https://spring.io/guides/gs/accessing-data-r2dbc/)
* [Building a Reactive RESTful Web Service](https://spring.io/guides/gs/reactive-rest-service/)
* [R2DBC Homepage](https://r2dbc.io)
Make sure to include a [R2DBC Driver](https://r2dbc.io/drivers/) to connect to your database.

-->
