#!/usr/bin/sh

PASSWD="password"
MAIN="user"
DBNAME="chatsystemdb"

echo "enter root password: "
read -sp rootpasswd

mariadb -uroot -p${rootpasswd} -e "CREATE DATABASE ${DBNAME} /*\!40100 DEFAULT CHARACTER SET utf8 */;"
mariadb -uroot -p${rootpasswd} -e "CREATE USER ${MAINDB}@localhost IDENTIFIED BY '${PASSWDDB}';"
mariadb -uroot -p${rootpasswd} -e "GRANT ALL PRIVILEGES ON ${DBNAME}.* TO '${MAINDB}'@'localhost';"
mariadb -uroot -p${rootpasswd} -e "FLUSH PRIVILEGES;"
