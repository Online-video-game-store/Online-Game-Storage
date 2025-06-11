## Создание контейнера с Keycloak, MySql и RabbitMQ.

В папке `docker-configs` создаем контейнеры из `docker-compose.yml`:
```shell
docker-compose up -d
```
Дождавшись полной инициализации `MySql` (1-10 минут при первом запуске),
увидим что-то вроде:
```shell
2025-06-10 14:35:35 2025-06-10 11:35:35+00:00 [Note] [Entrypoint]: Stopping temporary server
2025-06-10 14:35:58 2025-06-10 11:35:58+00:00 [Note] [Entrypoint]: Temporary server stopped
2025-06-10 14:35:58
2025-06-10 14:35:58 2025-06-10 11:35:58+00:00 [Note] [Entrypoint]: MySQL init process done. Ready for start up.
...
```

`MySql` автоматически выполнит `init.sql`, `schema.sql` и `data.sql`, создав все необходимые БД
и инициализировав некоторые.

Теперь можно запустить `Keycloak`, указав для этого его профиль:
```shell
docker-compose --profile manual up -d keycloak
```

### Альтернатива.
Можно разделить `docker-compose.yml` на два файла:
```shell
docker-compose.base.yml — без Keycloak
docker-compose.keycloak.yml — только Keycloak
```
И так же последовательно запускать их:
```shell
docker-compose -f docker-compose.base.yml up -d    # только mysql и rabbitmq
docker-compose -f docker-compose.keycloak.yml up -d  # потом — keycloak
```


## Настройка Keycloak.

Создаем папку под импорт в контейнере:
```shell
mkdir /opt/keycloak/data/import
```
Копируем туда файл с настройками (из текущей директории, можно прямо в IDEA):
```shell
docker cp ./online-store-realm-realm.json keycloak-ogs:/opt/keycloak/data/import/online-store-realm-realm.json
```
И в контейнере запускаем полный импорт:
```shell
/opt/keycloak/bin/kc.sh import --dir=/opt/keycloak/data/import --override true --features=scripts
```

В `UI` `Keycloak` должна появиться новая область, аналогичная создаваемой [вручную](Keycloak.md).


## Создание слепка настроек Keycloak.

Экспорт настроек `Keycloak`, для последующего быстрого перекидывания на другие машины,
можно осуществить следующим образом:

В контейнере:
````shell
/opt/keycloak/bin/kc.sh export --dir=/opt/keycloak/data/export --realm=online-store-realm --users realm_file
````
Копируем файл из контейнера в текущую папку (можно прямо из консоли IDEA):
```shell
docker cp keycloak-ogs:/opt/keycloak/data/export/online-store-realm-realm.json ./online-store-realm-realm.json
```
