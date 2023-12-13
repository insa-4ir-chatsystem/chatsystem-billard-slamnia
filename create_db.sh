#!/usr/bin/sh

PASSWD="password"
MAIN="user"
DBNAME="chatsystemdb"

read -s passwd
user=`whoami`

/home/slamnia/mysql/bin/mariadb -u$user -p${passwd} -P 3306 -e "CREATE DATABASE ${DBNAME} /*\!40100 DEFAULT CHARACTER SET utf8 */;"
/home/slamnia/mysql/bin/mariadb -u$user -p${passwd} -P 3306 -e "CREATE USER ${MAIN}@localhost IDENTIFIED BY '${PASSWD}';"
/home/slamnia/mysql/bin/mariadb -u$user -p${passwd} -P 3306 -e "GRANT ALL PRIVILEGES ON ${DBNAME}.* TO ${MAIN}@localhost;"
/home/slamnia/mysql/bin/mariadb -u$user -p${passwd} -P 3306 -e "FLUSH PRIVILEGES;"
