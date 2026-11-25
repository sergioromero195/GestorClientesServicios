# Pruebas del programa

- Probé algunas opciones para comprobar que la aplicación guarda los datos correctamente y avisa cuando hay algún problema.

## Clientes

- Dejé el nombre vacío y pulsé Guardar. La aplicación no guardó el cliente y mostró el mensaje «El nombre es obligatorio».
- Escribí un email sin formato válido. La aplicación lo rechazó y avisó de que el formato del email no era válido.

## Servicios

- Escribí un precio con más de dos decimales. La aplicación lo rechazó y mostró un aviso.
- Dejé el precio vacío y pulsé Guardar. La aplicación mostró «El precio es obligatorio» y no guardó el servicio.

## Reservas

- Probé una fecha pasada. La aplicación no permitió guardar la reserva.
- Escribí una fecha con formato incorrecto. La aplicación indicó el formato que debía usar.
- Probé la fecha `2026-02-30`. La aplicación avisó de que esa fecha no existe.
- Desmarqué una reserva y pulsé Actualizar o Eliminar. La aplicación me pidió seleccionar un registro.
- Seleccioné una reserva y pulsé Actualizar. La aplicación mostró que el registro se había actualizado.
- Seleccioné una reserva y pulsé Eliminar. La aplicación pidió confirmar antes de borrarla.

## Email duplicado

- Intenté guardar un cliente usando un email que ya existía. La aplicación lo rechazó y avisó de que ese email ya existe.
## Teléfono con formato no válido

- Intente guardar un cliente con un formato de teléfono del tipo "-------" y me avisó correctamente de que no tiene un formato válido.

## Precio máximo

- Probé el precio 100000000,00. La aplicación lo rechazó y mostró que el máximo es 99.999.999,99.

## descripción de servicio de más de 255 caracteres.

- Probé una descripcion de servicios de 256 caracteres y me avisó de no poder superar el límite de 255.

## Prueba del ejecutable

- Ejecuté `mvn clean package`; Maven terminó con `BUILD SUCCESS`.
- Abrí el programa usando el JAR generado. La aplicación se inició correctamente.nectó con MySQL y pude guardar un cliente correctamente.