
-- two databases, ff_portfolio_dev and ff_portfolio_prod
-- used defaults for schema/database setup
-- created users ff_dev_user and ff_prod_user, 
-- meant to do DML activities but not DDL



GRANT SELECT ON ff_portfolio_dev.* to 'ff_dev_user'@'localhost';
GRANT UPDATE ON ff_portfolio_dev.* to 'ff_dev_user'@'localhost';
GRANT INSERT ON ff_portfolio_dev.* to 'ff_dev_user'@'localhost';
GRANT DELETE ON ff_portfolio_dev.* to 'ff_dev_user'@'localhost';
GRANT SELECT ON ff_portfolio_prod.* to 'ff_prod_user'@'localhost';
GRANT INSERT ON ff_portfolio_prod.* to 'ff_prod_user'@'localhost';
GRANT DELETE ON ff_portfolio_prod.* to 'ff_prod_user'@'localhost';
GRANT UPDATE ON ff_portfolio_prod.* to 'ff_prod_user'@'localhost'; 
GRANT SELECT ON ff_portfolio_dev.* to 'ff_dev_user'@'%';
GRANT INSERT ON ff_portfolio_dev.* to 'ff_dev_user'@'%';
GRANT DELETE ON ff_portfolio_dev.* to 'ff_dev_user'@'%';
GRANT UPDATE ON ff_portfolio_dev.* to 'ff_dev_user'@'%';
GRANT SELECT ON ff_portfolio_prod.* to 'ff_prod_user'@'%';
GRANT INSERT ON ff_portfolio_prod.* to 'ff_prod_user'@'%';
GRANT DELETE ON ff_portfolio_prod.* to 'ff_prod_user'@'%';
GRANT UPDATE ON ff_portfolio_prod.* to 'ff_prod_user'@'%'; 