# Food Store - Sistema de Gestión de Pedidos de Comida

Este proyecto es el Trabajo Práctico Integrador para la materia **Programación 2** de la carrera **Tecnicatura Universitaria en Programación (UTN)**. Consiste en una aplicación de consola desarrollada en Java que implementa un sistema CRUD completo con persistencia en una base de datos relacional mediante JDBC puro.

## 👥 Integrantes del Grupo de trabajo
* Ignacio Cariaga
* Enzo Martínez
* Leonardo Palavecino
* Pablo Nogueira
* Bruno Tello

---

## 📺 Entrega y Demostración
* **Video Demostrativo:** [👉 Enlace al Video en YouTube/Drive]
* **Documentación Académica (PDF):** [👉 Enlace al PDF del Informe] *(O indicar si está guardado en la raíz como "Documentacion.pdf")*

---

## 🛠️ Tecnologías y Arquitectura Utilizadas
* **Lenguaje:** Java 21
* **Persistencia:** JDBC puro + MySQL
* **IDE Recomendado:** NetBeans
* **Patrón de Arquitectura:** Capas bien definidas:
  * "Entities": Modelo de dominio basado en el UML (Herencia de clase abstracta "Base").
  * "Dao": Acceso a datos independiente empleando el patrón DAO.
  * "Service": Lógica de negocio y reglas de validación.
  * "Ui / Main": Interfaz de usuario interactiva por consola.

---

## 🚀 Instrucciones de Configuración e Instalación

### 1. Requisitos Previos
* Tener instalado el **Java Development Kit (JDK 21)**.
* Tener instalado **MySQL Server** y un gestor (como MySQL Workbench o phpMyAdmin).
* **NetBeans IDE**.

### 2. Configuración de la Base de Datos
Antes de ejecutar la aplicación, es necesario levantar el esquema de la base de datos:
1. Crear una base de datos local llamada, por ejemplo, "pedidos_db".
2. Ejecutar el archivo de script SQL provisto en el proyecto ("schema.sql") para crear las tablas ("categoria", "producto", "usuario", "pedido", "detalle_pedido") e insertar los datos de prueba.

### 3. Configuración de la Conexión en Java
* Modificar el archivo de configuración centralizado (clase "ConexionDB.java" en paquete "Config").
* Asegurarse de colocar correctamente las credenciales locales:
  * **URL:** "jdbc:mysql://localhost:3306/pedidos_db"
  * **User:** "tu_usuario_de_mysql"
  * **Password:** "tu_contraseña_de_mysql"

### 4. Cómo Ejecutar el Proyecto en NetBeans
1. Abrir NetBeans IDE.
2. Seleccionar "File" -> "Open Project..." y elegir la carpeta raíz de este repositorio.
3. Hacer clic derecho sobre el proyecto en la barra lateral y seleccionar "Run" o presionar "F6".

---

## 📋 Funcionalidades Principales (Backlog Completado)
El sistema permite realizar operaciones CRUD completas bajo lógica de **Soft Delete** (Baja lógica usando el atributo "eliminado"):
* **Épica 1: Gestión de Categorías** (Listar, Crear, Editar, Baja Lógica).
* **Épica 2: Gestión de Productos** (Control de stock >= 0, precios válidos y asociación a categorías).
* **Épica 3: Gestión de Usuarios** (Validación de mail único y persistencia de roles).
* **Épica 4: Gestión de Pedidos y Detalles** (Carga de 1..N detalles usando transacciones seguras con Rollback en caso de fallas, cálculo automatizado del total mediante la interfaz "Calculable").