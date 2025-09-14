# Auth Service

Proyecto de autenticación con **Spring Boot** y **Gradle**.

## Requisitos previos
- **Java 8**.
- No es necesario tener Gradle instalado, ya que el proyecto incluye el *Gradle Wrapper* (`gradlew`).

## Compilar el proyecto

En la raíz del proyecto, ejecuta:

### Linux / macOS
```bash
./gradlew clean build
```

### Windows (PowerShell / CMD)
```bat
gradlew clean build
```

Esto generará el artefacto `.jar` dentro de:
```
build/libs/auth-service-<versión>.jar
```

## Ejecutar el proyecto

### Opción 1: Usando Gradle (modo desarrollo)
### Linux / macOS
```bash
./gradlew bootRun
```

### Windows (PowerShell / CMD)
```bat
gradlew bootRun
```

### Opción 2: Usando el JAR compilado
Primero compila el proyecto (ver pasos anteriores). Luego ejecuta:
```bash
java -jar build/libs/auth-service-<versión>.jar
```

Por defecto la aplicación se levanta en:
```
http://localhost:8080
```

## Endpoints principales

- **POST** `/api/auth/sign-up` → Registro de usuario.  
- **POST** `/api/auth/login` → Inicio de sesión (retorna JWT).  

El token devuelto debe enviarse en las siguientes llamadas protegidas mediante el header:

```
Authorization: Bearer <token>
```

## Base de datos

El proyecto está configurado para usar **H2 en memoria**.  
Puedes acceder a la consola en:
```
http://localhost:8080/h2-console
```

Valores por defecto:
- **JDBC URL**: `jdbc:h2:mem:authdb`
- **Usuario**: `sa`
- **Password**: *(vacío)*

## Ejecutar tests
### Linux / macOS
```bash
./gradlew test
```

### Windows (PowerShell / CMD)
```bat
gradlew test
```


