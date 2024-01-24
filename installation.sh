#!/usr/bin/sh

mkdir -pv ~/.cache/chatsystem/
touch ~/.cache/chatsystem/history.db
sqlite3 ~/.cache/chatsystem/history.db << EOF
CREATE TABLE IF NOT EXISTS contacts(id INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR(50) NOT NULL,ip VARCHAR(15) NOT NULL);

CREATE TABLE IF NOT EXISTS messages(id INTEGER PRIMARY KEY AUTOINCREMENT, message TEXT ,
      fileName TEXT , fileContent BLOB , type INTEGER NOT NULL CHECK (type IN (1,2,3)),
      origin INTEGER NOT NULL CHECK (type IN (1,2)), contactId INTEGER,
      FOREIGN KEY (contactId) REFERENCES contacts(id));
EOF
mvn package -f pom.xml
