USE security_2;

DROP TABLE if exists user_role;
DROP TABLE if exists roles;
DROP TABLE if exists users;

CREATE TABLE if not exists users (
    id int AUTO_INCREMENT PRIMARY KEY,
    name varchar(64) NOT NULL UNIQUE,
    email varchar(64) NOT NULL UNIQUE,
    password varchar(100) NOT NULL
);

CREATE TABLE if not exists roles (
    id int AUTO_INCREMENT PRIMARY KEY,
    role varchar(20) NOT NULL
);

CREATE TABLE if not exists user_role (
    user_id int,
    role_id int,
    FOREIGN KEY (user_id) references users(id),
    FOREIGN KEY (role_id) references roles(id)
);