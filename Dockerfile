#################################################
# Imagen base para el contenedor de compilación
#################################################
FROM --platform=linux/amd64 ubuntu:jammy
FROM maven:3.9.6-amazoncorretto-21 as builder

# Define el directorio de trabajo donde ejecutar comandos
WORKDIR /project

# Copia las dependencias del proyecto
COPY pom.xml /project/

# Descarga las dependencias del proyecto
#RUN mvn clean verify

# Copia el código del proyecto
COPY /src /project/src
COPY /images /project/images

# Compila proyecto
RUN mvn clean package -DskipTests=true

#################################################
# Imagen base para el contenedor de la aplicación
#################################################
FROM openjdk:21-jdk-slim

# Define el directorio de trabajo donde se encuentra el JAR
WORKDIR /usr/src/app/

# Descargamos el script wait-for-it.sh
RUN apt-get update && apt-get install -y \
    curl \
    && rm -rf /var/lib/apt/lists/* \
    && curl -LJO https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh \
    && chmod +x /usr/src/app/wait-for-it.sh

# Copia el JAR del contenedor de compilación
COPY --from=builder /project/target/ /usr/src/app/

# Indica el puerto que expone el contenedor
EXPOSE 8443
EXPOSE 587

# Comando que se ejecuta al hacer docker run
CMD [ "java", "-jar", "app.jar" ]