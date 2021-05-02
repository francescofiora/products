
CREATE SCHEMA IF NOT EXISTS STORE;

CREATE SEQUENCE STORE.SEQ_PRODUCT START 1;
CREATE SEQUENCE STORE.SEQ_CATEGORY START 1;
CREATE SEQUENCE STORE.SEQ_ORDER_ITEM START 1;
CREATE SEQUENCE STORE.SEQ_ORDER START 1;
CREATE SEQUENCE STORE.SEQ_USER START 1;

CREATE TABLE IF NOT EXISTS STORE.USER (
  id bigint NOT NULL PRIMARY KEY,
  username character varying(20) NOT NULL,
  password character varying(100) NOT NULL,
  role character varying(10) NOT NULL,
	enabled smallint default 1,
  account_non_expired smallint default 1,
  account_non_locked smallint default 1,
  credentials_non_expired smallint default 1
);

INSERT INTO STORE.USER (id,username,password,role,
  enabled,account_non_expired,account_non_locked,
  credentials_non_expired)
VALUES ('1','user','$2a$10$PMF.CrlbWKk/AdSmtKiOHu/5Fb9sZsEBSohppyoAKoNJeOhkoixzi',
  'USER',1,1,1,1);

INSERT INTO STORE.USER (id,username,password,role,
  enabled,account_non_expired,account_non_locked,
  credentials_non_expired)
VALUES ('2','admin','$2a$10$PMF.CrlbWKk/AdSmtKiOHu/5Fb9sZsEBSohppyoAKoNJeOhkoixzi',
  'ADMIN',1,1,1,1);

CREATE TABLE IF NOT EXISTS STORE.CATEGORY (
  id bigint NOT NULL PRIMARY KEY,
  description character varying(255) NOT NULL,
  name character varying(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS STORE.PRODUCT (
  id bigint NOT NULL PRIMARY KEY,
  description character varying(255) NOT NULL,
  image character varying(255),
  image_content_type character varying(255),
  name character varying(255) NOT NULL,			 	
  price numeric(21,2) NOT NULL,
  size character varying(255) NOT NULL,
  category_id bigint NOT NULL,
  CONSTRAINT fk1_product FOREIGN KEY (category_id) REFERENCES STORE.CATEGORY(id)
);

CREATE TABLE IF NOT EXISTS STORE.ORDER (
  id bigint NOT NULL PRIMARY KEY,
  code character varying(255) NOT NULL,
  customer character varying(255) NOT NULL,
  placed_date timestamp without time zone NOT NULL,
  status character varying(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS STORE.ORDER_ITEM (
  id bigint NOT NULL PRIMARY KEY,
  quantity integer NOT NULL CHECK (quantity >= 0),
  total_price numeric(21,2) NOT NULL,
  order_id bigint NOT NULL,
  product_id bigint NOT NULL,
  CONSTRAINT fk1_order_item FOREIGN KEY (product_id) REFERENCES STORE.PRODUCT(id),
  CONSTRAINT fk2_order_item FOREIGN KEY (order_id) REFERENCES STORE.ORDER(id)
);


