CREATE DATABASE sfg_dev;
CREATE DATABASE sfg_prod;

CREATE USER 'sfg_dev_user'@'localhost' IDENTIFIED BY 'mikael';
CREATE USER 'sfg_prod_user'@'localhost' IDENTIFIED BY 'mikael';

CREATE USER 'sfg_dev_user'@'%' IDENTIFIED BY 'mikael';
CREATE USER 'sfg_prod_user'@'%' IDENTIFIED BY 'mikael';

GRANT SELECT ON sfg_dev.* TO 'sfg_dev_user'@'localhost';
GRANT INSERT ON sfg_dev.* TO 'sfg_dev_user'@'localhost';
GRANT UPDATE ON sfg_dev.* TO 'sfg_dev_user'@'localhost';
GRANT DELETE ON sfg_dev.* TO 'sfg_dev_user'@'localhost';

GRANT SELECT ON sfg_dev.* TO 'sfg_dev_user'@'%';
GRANT INSERT ON sfg_dev.* TO 'sfg_dev_user'@'%';
GRANT UPDATE ON sfg_dev.* TO 'sfg_dev_user'@'%';
GRANT DELETE ON sfg_dev.* TO 'sfg_dev_user'@'%';

GRANT SELECT ON sfg_prod.* TO 'sfg_prod_user'@'localhost';
GRANT INSERT ON sfg_prod.* TO 'sfg_prod_user'@'localhost';
GRANT UPDATE ON sfg_prod.* TO 'sfg_prod_user'@'localhost';
GRANT DELETE ON sfg_prod.* TO 'sfg_prod_user'@'localhost';

GRANT SELECT ON sfg_prod.* TO 'sfg_prod_user'@'%';
GRANT INSERT ON sfg_prod.* TO 'sfg_prod_user'@'%';
GRANT UPDATE ON sfg_prod.* TO 'sfg_prod_user'@'%';
GRANT DELETE ON sfg_prod.* TO 'sfg_prod_user'@'%';
