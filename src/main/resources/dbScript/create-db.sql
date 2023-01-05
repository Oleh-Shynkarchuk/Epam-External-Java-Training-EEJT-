create TABLE certificates (
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(45) NOT NULL unique,
  description VARCHAR(45) DEFAULT NULL,
  price DECIMAL(10,2) DEFAULT NULL,
  duration INTEGER DEFAULT NULL,
  create_date TIMESTAMP DEFAULT NULL,
  last_update_date TIMESTAMP DEFAULT NULL
  );

create TABLE tags (
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(45) NOT NULL unique
  );
create TABLE certificates_has_tags (
  certificates_id INTEGER NOT NULL,
  tags_id INTEGER NOT NULL,
  CONSTRAINT  fk_certificates_has_tags_certificates FOREIGN KEY (certificates_id) REFERENCES certificates (id) on delete cascade,
  CONSTRAINT  fk_certificates_has_tags_tags FOREIGN KEY (tags_id) REFERENCES tags (id) on delete cascade
  );