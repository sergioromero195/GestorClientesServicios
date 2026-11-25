# Gestor de clientes, servicios y reservas

Este proyecto es una aplicación de escritorio para gestionar clientes, servicios y reservas. Permite añadir, consultar, modificar y eliminar registros.

## Tecnologías utilizadas

- Java 21
- Swing para las ventanas
- MySQL para guardar los datos
- JPA e Hibernate para trabajar con la base de datos
- Maven para gestionar las dependencias

## Preparar la base de datos

La aplicación se conecta a una base de datos local llamada `gestor_reservas_db`.

Para crear la base de datos y sus tablas, abre `database.sql` en MySQL Workbench y ejecútalo. El script utiliza `CREATE ... IF NOT EXISTS`, por lo que no borra tablas ni datos existentes.

Para cargar registros de ejemplo, ejecuta también `datos_prueba.sql` después de `database.sql`. Hazlo una sola vez en la base de datos preparada para la demostración.

Hibernate también está configurado para actualizar el esquema al iniciar la aplicación. La configuración de la conexión está en `src/main/resources/META-INF/persistence.xml`.

La aplicación necesita las variables de entorno `DB_USER` y `DB_PASSWORD`, con el usuario y la contraseña de MySQL. En IntelliJ se pueden añadir en la configuración de ejecución de `Main`. No se deben subir contraseñas reales al repositorio.

## Ejecutar la aplicación desde IntelliJ

1. Abre el proyecto en IntelliJ IDEA.
2. Espera a que Maven cargue las dependencias de `pom.xml`.
3. Comprueba que MySQL Server está iniciado y que la conexión está configurada.
4. Ejecuta la clase `Main`.

## Uso

Desde la ventana principal se puede acceder a la gestión de clientes, servicios y reservas. En las reservas se selecciona un cliente y un servicio existentes. Los estados disponibles son Pendiente, Confirmada y Cancelada.

## Organización del proyecto

- `config`: configuración de JPA.
- `controller`: conecta las ventanas con la lógica de la aplicación.
- `dao`: operaciones de acceso a la base de datos.
- `model`: entidades Cliente, Servicio y Reserva.
- `service`: lógica de gestión y validaciones.
- `view`: ventanas de la aplicación.

## Ejecutar el JAR

Se necesita tener Java 21 o posterior y MySQL Server instalado y en ejecución.

1. Abre `database.sql` en MySQL Workbench y ejecútalo para crear la base de datos y sus tablas.
2. Abre PowerShell en la carpeta principal del proyecto.
3. Genera el JAR con Maven:

   ```powershell
   mvn clean package
   ```

   Al terminar, el archivo estará en `target/gestor-reservas-1.0-SNAPSHOT.jar`.

4. En esa misma ventana de PowerShell, configura las credenciales de tu usuario de MySQL. Sustituye los ejemplos por tus propios datos:

   ```powershell
   $env:DB_USER = "usuario_mysql"
   $env:DB_PASSWORD = "contraseña_mysql"
   ```

5. Inicia la aplicación desde esa misma ventana:

   ```powershell
   java -jar .\target\gestor-reservas-1.0-SNAPSHOT.jar
   ```