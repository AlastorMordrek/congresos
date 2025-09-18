# CONGRESOS
Sistema de inscripciones y control de acceso para congresos y conferencias.

## Prerequisitos
Antes de correr esta aplicacion, asegurese de tener instaladas las siguiented aplicaciones en su sistema.

 - Docker
 - Docker Compose
 - Git
 - Java 24 (for local development without Docker)

## Inicio rapido con docker
La forma mas rapida de iniciar es con docker-compose.

### 1.- Clona el repositorio
```shell
git clone https://github.com/AlastorMordrek/congresos.git
cd congresos
```
### 2.- Ajusta variables de entorno
```shell
cp .env.example .env
# Edite el archivo .env con sus parametros preferidos
```
### 3.- Ejecute el script de arranque
```shell
chmod +x setup.sh
./setup.sh
```
### 4.- Acceda a la aplicacion
El servidor estara escuchando peticiones http en el puerto especificado
en su archivo .env (default 8080) http://localhost:8080

## Inicio manual
Si prefiere correr la aplicacion sin docker.

### 1.- Asegurese que PostgreSQL esta corriendo
Cree la base de datos.
```shell
createdb congresos
```
### 2.- Configurar conexion de base de datos
Actualice el archivo de configuracion
`src/main/resources/application.properties`
con sus credenciales de base de datos.
Debe usar un usuario y clave de postgres.

### 3.- Compile y corra la aplicacion
Compilar.
```shell
./mvnw clean package
```
Correr.
```shell
java -jar target/*.jar
```

## Configuracion
La aplicacion provee y usa variables de configuracion para personalizar
el funcionamiento.

Variables clave incluyen:
 - DB_USR: Usuario de postgres (default: `admin`)
 - DB_PWD: Clave de acceso de postgres (default: `12345`)
 - DB_HOST: Host de postgres (default: `localhost`)
 - DB_PORT: Puerto de postgres (default: `5432`)
 - DB_NAME: Nombre de la base de datos (default: `congresos`)
 - WEB_SERVER_PORT: Puerto del servidor web (default: `8080`)

## Usuarios Pre-registrados
El sistema viene con usuarios pre-registrados para distintos roles:

 - Administrador: `congresos.administrador.1@tijuana.tecnm.mx`
 - Organizador: `congresos.organizador.1@tijuana.tecnm.mx`
 - Staff: `congresos.staff.1@tijuana.tecnm.mx` / `congresos.staff.2@tijuana.tecnm.mx`
 - Alumnos: `congresos.alumno.1@tijuana.tecnm.mx` / `congresos.alumno.2@tijuana.tecnm.mx`

#### Claves de acceso
La clave de acceso por defecto de todos los usuarios pre-regisrados es `12345`

## API Documentation
Cuando la aplicacion este corriendo, puede acceder a la documentacion
de APIs generada por swagger en:
http://localhost:8080/swagger-ui.html

## Estructura del proyecto

```shell
congresos/
├── src/main/java/       # Codigo de la aplicacion de springboot
├── src/main/resources/  # Archivos de configuracion
├── docker-compose.yml   # Configuracion del contenedor de docker
├── Dockerfile           # Definicion del contenedor de aplicacion
├── setup.sh             # Script de configuracion automatica
├── .env.example         # Plantilla de variables de configuracion
└── README.md            # Este archivo
```

## Problemas tipicos
 - Conflicto de puertos: Si el puerto 8080 ya esta en uso, cambie la variable WEB_SERVER_PORT en su archivo .env
 - Problemas de conexion de la base de datos: Asegurese que PostgreSQL esta corriendo y accesible
 - Permisos de Docker: En Linux puede que deba correr comandos de Docker con `sudo`
 - Variables de ambiente: Asegurese que todas las variables relevantes estan ajustadas en su archivo .env

## Licencia
Este proyecto esta desarrollado para fines educativos y uso exclusivo
del TecNM - Instituto Tecnológico de Tijuana.
