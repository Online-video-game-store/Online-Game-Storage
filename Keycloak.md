# Развертывание и настройка Keycloak.

## 1. Создание контейнера с Keycloak.

Используется в связке с MySQL, хотя это не принципиально и можно заменить на любую аналогичную БД. 

Создаем docker-compose.yml:

```dockerfile
version: '3'
services:
  mysql:
    image: mysql:8.0
    container_name: mysql
    ports:
      - "3306:3306"
    restart: unless-stopped
    volumes:
      - keycloak-and-mysql-volume:/var/lib/mysql
    environment:
      MYSQL_ROOT_PASSWORD: admin
      MYSQL_USER: admin
      MYSQL_PASSWORD: admin
      MYSQL_DATABASE: keycloak_db
    networks:
      - keycloak-network

  keycloak:
    image: quay.io/keycloak/keycloak:latest
    ports:
      - "8080:8080/tcp"
    command: start-dev --http-port=8080
    restart: unless-stopped
    environment:
      KEYCLOAK_ADMIN: admin
      KEYCLOAK_ADMIN_PASSWORD: admin
      DB_VENDOR: mysql
      DB_ADDR: jdbc:mysql://mysql:3306/keycloak_db
      DB_USER: admin
      DB_PASSWORD: admin
    depends_on:
      mysql:
        condition: "service_started"
    networks:
      - keycloak-network

networks:
  keycloak-network:
    driver: bridge
```
Собираем и запускаем контейнер:
```shell
docker-compose up -d
```


## 2. Настройка Keycloak.


Заходим на http://localhost:8080.


### 1) Создаем свою область - realm. Назовем её `online-store-realm`.


### 2) Создаем клиента для авторизации пользователей:

a) Clients -> Create client:
```text
    client-id                       = online-store-client-id
    Valid redirect URIs             = http://localhost:9000/*
    Valid post logout redirect URIs = http://localhost:9000/*
    Web origins                     = http://localhost:9000
    Client authentication           = On
    Authorization                   = On
```

b) На вкладке `Roles` создаем роли: ADMIN, USER, DEVELOPER.

c) На вкладке `Client scopes`:
- В списке `scopes` переходим на `online-store-client-id-dedicated`.
- Переходим на вкладку Scope и задаем флаг `Full scope allowed` в положение `Off`.

Это приведет к тому, что в токен будут попадать только те роли, что присвоены конкретному пользователю.


### 3) Создаем пользователей, хотя бы админа:
`Users` -> `Add user`:

На вкладке `Credentials` задаем пароль, а на вкладке `Role mapping` задаем пользователю роли.


### 4) Настраиваем создание новых пользователей:
`Realm setting` -> `User registration` -> `Assign role`:

добавляем роль `USER`, чтобы каждый новый пользователь получал эту роль.


### 5) Добавляем отображение ролей в токене, в удобном нам атрибуте:
`Client scopes` -> `Create client scope`:

Создаем права для ролей (назовем `online-store-user-roles`), не забывая устанавливать флаг `Include in token scope` в `Off`.

На вкладке `Mappers` -> `Add mapper` создаем маппер `User Client Role`, со следующими свойствами:
```text
Name = online-store-user-roles-mapper
Client ID = online-store-client-id
Client Role prefix = ROLE_
Multivalued = On
Token Claim Name = roles
```

Далее, добавляем эти права (scope) в `Clients` -> `Client scope` -> `Add scope`.

Теперь все роли в токене будут собраны в список и помещены в поле "roles".


### 6) Привязываем к ролям права:
`Client scopes` -> `Create client scope`:

Создаем скопы `read`, `write`, `update`, `delete`, `rename`, с флагом `Include in token scope` установленным в `ON`.

Переходим на вкладку `Scope` -> `Assign role` и добавляем роли, к которым будет привязано данное право (scope).

Например: read - роли ADMIN, USER, DEVELOPER. write - роли ADMIN, DEVELOPER.

Далее, разрешаем эти скопы у нашего клиента:

`Clients` -> `online-store-client-id` -> `Client scopes` -> `Add client scope`. выбираем скоп, созданный выше и добавляем с `Assigned type = Default`.

Теперь в токене `scopes` будут прописаны в зависимости от роли пользователя. 

Такая настройка позволяет манипулировать как ролями, так и отдельными разрешениями. Например, можно понизить какие-то права конкретному пользователю, не меняя его роли. Разумеется программа тоже должна уметь это делать.
