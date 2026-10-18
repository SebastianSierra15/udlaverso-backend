# UdlaVerso Backend

API REST de UdlaVerso con autenticación, validación, persistencia y documentación de la API para la plataforma de la Universidad de la Amazonia.

## Stack

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- MySQL
- JWT
- Swagger / OpenAPI
- Docker

## Arquitectura

El backend organiza el acceso HTTP, la lógica de negocio, la persistencia y la seguridad en capas separadas. La autenticación se basa en JWT y la API se documenta mediante OpenAPI.

## Desarrollo local

En Windows:

```bash
mvnw.cmd spring-boot:run
```

En macOS o Linux:

```bash
./mvnw spring-boot:run
```

## Tests y empaquetado

```bash
mvnw.cmd test
mvnw.cmd clean package
```

La documentación OpenAPI está disponible en la ruta configurada por la aplicación cuando el servicio está ejecutándose.

## Docker

```bash
docker build -t udlaverso-backend .
docker run --env-file .env -p 8080:8080 udlaverso-backend
```

No incluyas credenciales ni archivos `.env` en el repositorio. Usa variables de entorno para la configuración local y de producción.

## Autor

[Sebastián Sierra](https://github.com/SebastianSierra15)