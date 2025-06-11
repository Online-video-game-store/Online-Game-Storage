## Создание контейнера с Keycloak, MySql и RabbitMQ.

В папке docker-configs запускаем docker-compose.yml:
```shell
docker-compose up -d
```
Дождавшись полной инициализации MySql (5-10 минут при первом запуске),
увидим что-то вроде:
```shell
2025-06-10 14:35:35 2025-06-10 11:35:35+00:00 [Note] [Entrypoint]: Stopping temporary server
2025-06-10 14:35:58 2025-06-10 11:35:58+00:00 [Note] [Entrypoint]: Temporary server stopped
2025-06-10 14:35:58
2025-06-10 14:35:58 2025-06-10 11:35:58+00:00 [Note] [Entrypoint]: MySQL init process done. Ready for start up.
2025-06-10 14:35:58
```
Далее запускаем Keycloak:
```shell
docker-compose --profile manual up -d keycloak
```

### Альтернатива.
Можно разделить docker-compose.yml на два файла:
```shell
docker-compose.base.yml — без Keycloak
docker-compose.keycloak.yml — только Keycloak
```
И так же последовательно запускать их:
```shell
docker-compose -f docker-compose.base.yml up -d    # только mysql и rabbitmq
docker-compose -f docker-compose.keycloak.yml up -d  # потом — keycloak
```

## Создание слепка настроек Keycloak.
В контейнере:
````shell
/opt/keycloak/bin/kc.sh export --dir=/opt/keycloak/data/export --realm=online-store-realm --users realm_file
````
Копируем файл из контейнера в текущую папку:
```shell
docker cp keycloak-ogs:/opt/keycloak/data/export/online-store-realm-realm.json ./online-store-realm-realm.json
```

## Импорт настроек в Keycloak.
Создаем папку под импорт в контейнере:
```shell
mkdir /opt/keycloak/data/import
```
Копируем туда файл с настройками (из текущей директории, прямо в IDEA):
```shell
docker cp ./online-store-realm-realm.json keycloak-ogs:/opt/keycloak/data/import/online-store-realm-realm.json
```
И в контейнере запускаем полный импорт:
```shell
/opt/keycloak/bin/kc.sh import --dir=/opt/keycloak/data/import --override true --features=scripts
```


## Настройка на Keycloak.

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
