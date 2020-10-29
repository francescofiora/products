
CREATE SCHEMA IF NOT EXISTS STORE;

CREATE SEQUENCE STORE.SEQ_PRODUCT START 1;
CREATE SEQUENCE STORE.SEQ_CATEGORY START 1;
CREATE SEQUENCE STORE.SEQ_ORDER_ITEM START 1;
CREATE SEQUENCE STORE.SEQ_ORDER START 1;


CREATE TABLE IF NOT EXISTS STORE.CATEGORY (
  id bigint NOT NULL PRIMARY KEY,
  description character varying(255),
  name character varying(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS STORE.PRODUCT (
  id bigint NOT NULL PRIMARY KEY,
  description character varying(255),
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


