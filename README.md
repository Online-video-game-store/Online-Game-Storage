## Keycloak

Установка и настройка: [Keycloak.md](Keycloak.md)


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


## MySql
Чтобы не плодить новых контейнеров с БД, решил использовать ту, что уже стоит
в связке с Keycloak. Можно конечно заново пересоздать контейнер, заложив в него
создание всех нужных БД, но если не хочется терять данных Keycloak, то можно
сделать это ручками в контейнере.

```shell
mysql -uroot -padmin
CREATE DATABASE IF NOT EXISTS payment_db;
GRANT ALL PRIVILEGES ON payment_db.* TO 'admin'@'%';
FLUSH PRIVILEGES;
```
И так для всех БД (для каждого микросервиса).

Или создать init.sql и запустить его:
```shell

mysql -uroot -padmin < init.sql
```

TODO: подправить docker-compose.yml, внедрив init.sql с созданием всех БД.


docker run --rm -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin -v D:/Users/Andrey/AppData/IdeaProjects/Online-Game-Storage/exports:/opt/keycloak/data/export quay.io/keycloak/keycloak:latest export --dir=/opt/keycloak/data/export --realm=online-store-realm --users=realm_file
docker-compose up -d
