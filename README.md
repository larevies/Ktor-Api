# Ktor HTTP API

**HTTP API** был создан для связи базы данных **PostgreSQL** 
и мобильного **Android-приложения** [Investio](https://github.com/Kezy-Kuroyame/Investio), написанного на языке 
программирования **Kotlin** с использованием **Ktor**, 
фреймворка для создания микросервисов и веб-приложений.

Авторы:
- **Софья Быковченко**
- **Валерия Серебренникова**

## Как пользоваться API?

Чтобы сервер заработал, нужно запустить 
[приложение](src/main/kotlin/com/example/Application.kt).

Данные для соединения с базой данных необходимо указать 
[здесь](src/main/kotlin/com/example/database/connection/DBProperties.kt).

### Распространённые ошибки

В [файле конфигурации](src/main/resources/application.conf) 
указаны настройки порта, на котором по умолчанию запускается 
сервер. ЕСЛИ будет ошибка вида "порт недоступен", поменяйте 
в файле конфигурации номер порта.

Если IDE не воспринимает SQL-запросы, наводим мышкой на запрос,
там предложат выбрать базу данных с компьютера. Точных слов 
не помню, но откроется окно, в нём нужно будет указать 
название хоста (localhost), порт, имя владельца 
(postgres по умолчанию) и пароль.


### Тесты

**HTTP запросы** (**GET, POST, DELETE**) в трёх файлах лежат 
в [папке с тестами](src/test/kotlin). 



## О Базе Данных
Логика работы с базой данных описана 
[здесь](src/main/kotlin/com/example/database).

В папке 
[connection](src/main/kotlin/com/example/database/connection) 
описано подключение к базе данных. 
В файле 
[DBConnection.kt](src/main/kotlin/com/example/database/connection/DBConnection.kt) 
есть функция main. В ней много закомментированных команд 
по работе с базой данных. Их функционал описан в том 
же файле, можно воспользоваться ими, запуская 
DBConnection.kt.

В папке 
[queries](src/main/kotlin/com/example/database/queries) 
содержатся функции с SQL запросами.

## О строении API
В папке 
[modules](src/main/kotlin/com/example/modules) 
содержатся классы, по структуре одинаковые
с сущностями из базы данных.

В папке 
[plugins](src/main/kotlin/com/example/plugins) 
хранятся вещи, необходимые для корректной 
работы программы (json-сериализация, конфигурация 
маршрутов).

В папке 
[routes](src/main/kotlin/com/example/routes) 
хранятся http-запросы.

## Примеры
Примеры работы с API (1 - ID объекта):
 ```
###
POST http://127.0.0.1:8080/company
Content-Type: application/json
{
  "name" : "26",
  "current_price": 110.0
}
Response:
"Company added correctly", status = 201


###
GET http://127.0.0.1:8080/company/1
Response:
{
  "name" : "26",
  "current_price": 110.0
}

###
DELETE http://127.0.0.1:8080/company/1
Response:
"Company removed correctly", status = 202
 ```
