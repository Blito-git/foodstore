CREATE TABLE IF NOT EXISTS categorias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    eliminado TINYINT(1) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS productos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    precio DOUBLE NOT NULL,
    descripcion TEXT,
    stock INT NOT NULL,
    imagen VARCHAR(255),
    disponible TINYINT(1) DEFAULT 1,
    categoria_id BIGINT,
    eliminado TINYINT(1) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (categoria_id) REFERENCES categorias(id)
);

CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    mail VARCHAR(150) NOT NULL UNIQUE,
    celular VARCHAR(50),
    contrasena VARCHAR(255) NOT NULL,
    rol VARCHAR(30) NOT NULL,
    eliminado TINYINT(1) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS pedidos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    fecha DATE NOT NULL,
    estado VARCHAR(30) NOT NULL,
    total DOUBLE NOT NULL,
    forma_pago VARCHAR(30) NOT NULL,
    eliminado TINYINT(1) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

CREATE TABLE IF NOT EXISTS detalles_pedido (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pedido_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    cantidad INT NOT NULL,
    subtotal DOUBLE NOT NULL,
    FOREIGN KEY (pedido_id) REFERENCES pedidos(id),
    FOREIGN KEY (producto_id) REFERENCES productos(id)
);


INSERT INTO categorias (nombre, descripcion) VALUES 
('Hamburguesas', 'Variedad de hamburguesas caseras con papas'),
('Pizzas', 'Pizzas al molde y a la piedra'),
('Bebidas', 'Gaseosas, aguas y jugos naturales');

INSERT INTO productos (nombre, precio, descripcion, stock, disponible, categoria_id) VALUES 
('Hamburguesa Clásica', 4500.00, 'Medallón de carne, queso y lechuga', 20, 1, 1),
('Pizza Muzzarella', 6000.00, 'Salsa de tomate y abundante muzzarella', 15, 1, 2),
('Gaseosa Cola 500ml', 1200.00, 'Bebida bien fría', 50, 1, 3);

INSERT INTO usuarios (nombre, apellido, mail, celular, contrasena, rol) VALUES 
('Carlos', 'Gomez', 'admin@foodstore.com', '2615551234', 'admin123', 'ADMINISTRADOR'),
('Ana', 'Lopez', 'operador@foodstore.com', '2615555678', 'ope123', 'OPERADOR'),
('Juan', 'Perez', 'juan@gmail.com', '2615559876', 'juan123', 'CLIENTE');