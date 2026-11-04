# Gestor de clientes, servicios y reservas

Este proyecto es una aplicación de escritorio para gestionar clientes, servicios y reservas. Permite añadir, consultar, modificar y eliminar registros.

## Tecnologías utilizadas

- Java 21
- Swing para las ventanas
- MySQL para guardar los datos
- JPA e Hibernate para trabajar con la base de datos
- Maven para gestionar las dependencias

## Preparar la base de datos

La aplicación se conecta a una base de datos local llamada `gestor_reservas_db`. Para crear la base de datos y sus tablas, abre `database.sql` en MySQL Workbench y ejecútalo. El script usa `CREATE ... IF NOT EXISTS`, por lo que no borra tablas ni datos existentes. Hibernate también está configurado para actualizar el esquema al iniciar la aplicación. La configuración de la conexión está en `src/main/resources/META-INF/persistence.xml`.

También hay que configurar las variables de entorno `DB_USER` y `DB_PASSWORD` con el usuario y la contraseña de MySQL. En IntelliJ se pueden añadir en la configuración de ejecución de `Main`. No se deben subir contraseñas reales al repositorio.

## Ejecutar la aplicación

1. Abrir el proyecto en IntelliJ IDEA.
2. Esperar a que Maven cargue las dependencias del `pom.xml`.
3. Comprobar que el servidor MySQL está iniciado y que la conexión está configurada.
4. Ejecutar la clase `Main`.

## Uso

Desde la ventana principal se puede acceder a la gestión de clientes, servicios y reservas. En las reservas se selecciona un cliente y un servicio existentes. Los estados disponibles son Pendiente, Confirmada y Cancelada.

## Organización del proyecto

- `config`: configuración de JPA.
- `controller`: conecta las ventanas con la lógica de la aplicación.
- `dao`: operaciones de acceso a la base de datos.
- `model`: entidades Cliente, Servicio y Reserva.
- `service`: lógica de gestión y validaciones.
- `view`: ventanas de la aplicación.
