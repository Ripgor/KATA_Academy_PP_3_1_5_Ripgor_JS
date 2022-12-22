USE security_2;

-- password = rinmin
INSERT INTO roles (role) values ('ROLE_ADMIN'), ('ROLE_USER');

INSERT INTO users (name, email, password) values ('Ripgor', 'ripgor@gmail.com', '$2a$12$lU3ymw7I06q8VC9VLC4cb.wS0GVmhP0kprp92HhCYhEAYyOFJ3zJW');

INSERT INTO user_role (user_id, role_id) values (1, 1);