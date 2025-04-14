## Keycloak

Для развертывания в продакшен необходимо установить переменную среды окружения:
`OAUTH2_BASE_ISSUER_URI=http://<адрес сервера Keycloak>:порт`

Например:

`OAUTH2_BASE_ISSUER_URI=http://192.168.1.179:8080`

Также необходимо задать пути для пользовательской авторизации:

`OAUTH2_BASE_AUTH_URI=http://<базовый адрес клиента>:порт`

Например:

`OAUTH2_BASE_AUTH_URI=http://192.168.1.179:9000`


## Eureka

Для развертывания в продакшен необходимо установить переменную среды окружения:
`EUREKA_SERVER_URL`

Если конечно эврика располагается на другом сервере, нежели остальные микросервисы.


## Catalog-service
Базовый путь к изображениям задан в переменной app.images-path. Окончательный путь формируется как:
{app.images-path}/{productId}/{filename.pic}
где: productId - уникальный ID товара.
При удалении товара удаляется весь подкаталог с содержимым.



```shell
docker run -d --name keycloak -p 8080:8080 -e KC_BOOTSTRAP_ADMIN_USERNAME=admin -e KC_BOOTSTRAP_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:latest start-dev
```


## Spring Cloud Bus + RabbitMQ (с Management Plugin)

Запуск RabbitMQ (можно прямо из консоли IDEA):

```shell
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:management
```
Порт 5672 — стандартный порт для AMQP-протокола.

Порт 15672 — для веб-интерфейса RabbitMQ Management.

Админка соответственно: http://localhost:15672

Пароль и логин: `guest`
