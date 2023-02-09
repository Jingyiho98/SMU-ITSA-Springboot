# Memberson Middleware
## Description:
> Middleware server that handles requests to Memberson CRM.

## Modules:
1. api
> This is where models and API definition interfaces are defined.
2. controller
> This is where controllers, services, dao, repositories & utils are created.
3. entrypoint
> This is where main class is.

---

## Run on localhost:
- ### Step by step
1. Check application-localhost.properties.
2. Run entrypoint > Entrypoint.java.
3. Go to [`http://localhost:8080/memberson/swagger-ui.html`](http://localhost:8080/memberson/swagger-ui.html) for API docs and to test your APIs.

---

## Run on docker:
- ### Prerequisites
> 1. Docker running on your computer.
- ### Step by step
1. Check configurations in [`docker/config.env`](docker/config.env).
2. Build docker image locally and push to docker repository.
> bash docker/build.sh
3. Run docker image.
> bash docker/run.sh
4. Go to [`http://localhost:8080/memberson/swagger-ui.html`](http://localhost:8080/memberson/swagger-ui.html) and test your APIs.
---