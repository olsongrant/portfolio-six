GRANT SELECT ON ff_aws_one.* to 'ff_aws_user'@'%';
GRANT INSERT ON ff_aws_one.* to 'ff_aws_user'@'%';
GRANT DELETE ON ff_aws_one.* to 'ff_aws_user'@'%';
GRANT UPDATE ON ff_aws_one.* to 'ff_aws_user'@'%';

GRANT SELECT ON ff_portfolio_dev.* to 'ff_dev_user'@'%';
GRANT INSERT ON ff_portfolio_dev.* to 'ff_dev_user'@'%';
GRANT DELETE ON ff_portfolio_dev.* to 'ff_dev_user'@'%';
GRANT UPDATE ON ff_portfolio_dev.* to 'ff_dev_user'@'%';

GRANT SELECT ON ff_portfolio_prod.* to 'ff_prod_user'@'%';
GRANT INSERT ON ff_portfolio_prod.* to 'ff_prod_user'@'%';
GRANT DELETE ON ff_portfolio_prod.* to 'ff_prod_user'@'%';
GRANT UPDATE ON ff_portfolio_prod.* to 'ff_prod_user'@'%';

