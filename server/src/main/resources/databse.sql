CREATE TABLE Clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ip VARCHAR(45) NOT NULL
);

CREATE TABLE Administradores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255),
    usuario VARCHAR(255) UNIQUE,
    contraseña VARCHAR(255),
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    ultima_sesion DATETIME
);

CREATE TABLE Conexiones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT,
    administrador_id INT,
    fecha_conexion DATETIME,
    fecha_desconexion DATETIME,
    duracion INT,
    cantidad_clicks INT,
    FOREIGN KEY (cliente_id) REFERENCES Clientes(id)
    FOREIGN KEY (administrador_id) REFERENCES Administradores(id)
);

CREATE TABLE Videos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    administrador_id INT,
    cliente_id INT,
    ruta_video VARCHAR(255),
    FOREIGN KEY (cliente_id) REFERENCES Clientes(id),
    FOREIGN KEY (administrador_id) REFERENCES Administradores(id)
);

CREATE TABLE Reportes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    conexiones_id INT,
    contenido TEXT,
    FOREIGN KEY (conexiones_id) REFERENCES Conexiones(id)
);


