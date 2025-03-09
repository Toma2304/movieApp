DROP TABLE IF EXISTS favorites;
DROP TABLE IF EXISTS credentials;

CREATE TABLE credentials (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             email VARCHAR(255) NOT NULL UNIQUE,
                             password VARCHAR(255) NOT NULL
);

CREATE TABLE favorites (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           item_id INT NOT NULL,
                           title VARCHAR(255) NOT NULL,
                           poster VARCHAR(255) NOT NULL,
                           watched BOOLEAN NOT NULL DEFAULT FALSE,
                           finish_date DATE,
                           impression LONGTEXT,
                           rating INTEGER,
                           user_id BIGINT,

                           CONSTRAINT fk_favorites_user
                               FOREIGN KEY (user_id) REFERENCES credentials(id)
);

-- password : 12345678
INSERT INTO credentials (email, password) VALUES ('test@test.com', '$2a$10$G35zlmleYLRQiaf1DepJg.VDw6tSWTFcFtj0WQ.UzGb9fVO.PQkS.');
