# FoodStore

Trabajo Práctico Integrador (TPI) - **Universidad Tecnológica Nacional (UTN)**.

Este sistema consiste en una aplicación de consola escrita en **Java 21** para la persistencia de datos en un motor **MySQL**. Implementa una arquitectura multicapa.

---

## Enlace
>
> [ HACÉ CLIC ACÁ PARA VER EL VIDEO ](COPIAR_EL_LINK_DE_YOUTUBE_O_DRIVE_AQUÍ)

---

## Integrantes del Equipo

| Apellido   | Nombre   |
| :---       | :---     |
| Nogueira   | Pablo    |
| Tello      | Bruno    |

---

## Requisitos Previos

Antes de clonar y ejecutar la aplicación, asegúrate de contar con el siguiente entorno instalado:
*   **Java Development Kit (JDK):** Versión 21 o superior.
*   **Gestor de Base de Datos:** MySQL Server (disponible mediante XAMPP, WampServer o instalación nativa).
*   **IDE Sugerido:** IntelliJ IDEA, Eclipse o Visual Studio Code (con la extensión Pack para Java).

---

## Instalación y Despliegue Paso a Paso

Sigue estas instrucciones para levantar el entorno de desarrollo de manera local:

### Paso 1: Clonar el Repositorio
Abre tu terminal y ejecuta el comando de clonación:
```bash
git clone [https://github.com/Blito-git/foodstore.git](https://github.com/Blito-git/foodstore.git)
cd foodstore
Paso 2: Configurar e Importar la Base de Datos

Inicia tu servidor MySQL.

Abre tu herramienta de gestión de bases de datos.

Crea un nuevo esquema vacío con el nombre exacto de: food_store.

Busca el archivo schema.sql (incluido en la raíz de este proyecto) y ejecútalo dentro del esquema para levantar la estructura de tablas (categorias, productos, usuarios, pedidos, detalles_pedido) y los datos de prueba preestablecidos.

Paso 3: Configurar Credenciales de Conexión

Si tu servidor MySQL local tiene una contraseña asignada para el usuario root o utiliza un puerto diferente al 3306, edita las propiedades de conexión en el siguiente archivo:
📁 src/resources/db.properties

Properties
db.url=jdbc:mysql://localhost:3306/food_store
db.usuario=root
db.password=TU_CONTRASEÑA_AQUÍ

Paso 4: Compilar y Ejecutar la Aplicación

Abre el proyecto completo en tu IDE, espera a que cargue el árbol de dependencias, busca el archivo ejecutable raíz en:
📁 src/com/foodstore/Main.java

Inicia el menú interactivo en la terminal del sistema.