# webflux-1
Общий принцип работы:

При запуске с помощью flyway создаются таблицы в бд postgreSQL на основе скриптов по пути src\main\resources\db

Настройки для своей БД нужно прописать в файле application.yaml

После запуска нужно зарегистрировать нового пользователя:

url: http://localhost:9001/api/auth/register

Пример json для создания пользователя: 
{ "username": "test1", "password": "test1","first_name": "AAAA","last_name": "KKKK"}

![image](https://github.com/Knouton/webflux-1/assets/19289934/d178c165-3551-4e6b-8e83-e2385a28f1d0)

Затем нужно получить JWT 

url: http://localhost:9001/api/auth/login
Пример json для запроса JWT: 

{"username": "test1", "password": "test1"}
![image](https://github.com/Knouton/webflux-1/assets/19289934/2a864b3c-e84f-48d6-a7a9-ca836d95b959)

Пример ответа:
{
    "user_id": 5,
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiVVNFUiIsInVzZXJuYW1lIjoidGVzdDEiLCJpc3MiOiJpc3N1ZXIiLCJzdWIiOiI1IiwiaWF0IjoxNjkyMjU2MTczLCJqdGkiOiI4N2Y3ZmQyYS1hNzY4LTQ1YjctOTZlNS1kNTg5OWI5ZmMwZjgiLCJleHAiOjE2OTIyNTk3NzN9.VCkQZguKO4oitbFJ9lQbwGuOYdOEVSNU9eGxNf7mxZw",
    "issue_at": "2023-08-17T07:09:33.798+00:00",
    "expires_at": "2023-08-17T08:09:33.798+00:00"
}

Затем используя JWT можем заходить по другим url:
http://localhost:9001/api/auth/info
![image](https://github.com/Knouton/webflux-1/assets/19289934/4a18bc50-8ed5-4e29-bae5-6ba2e7c9c95b)

Можно создать ресурс по url(используя авторизацию через JWT):

http://localhost:9001/resource

Пример json:

{"value":"value","path":"path"}
![image](https://github.com/Knouton/webflux-1/assets/19289934/dd12888a-f14a-40be-aefc-dedc1588d7de)

Затем получим созданный нами ресурс(используя авторизацию через JWT):

http://localhost:9001/resource/2
![image](https://github.com/Knouton/webflux-1/assets/19289934/fecd303d-bb38-4200-863d-f3293ccc9986)

