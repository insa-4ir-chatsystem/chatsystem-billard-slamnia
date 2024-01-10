#!/usr/bin/sh

mkdir -pv ~/.cache/chatsystem/
touch ~/.cache/chatsystem/history.db
sqlite3 ~/.cache/chatsystem/history.db << EOF
CREATE TABLE contacts(id INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR(50) NOT NULL,ip VARCHAR(15) NOT NULL);

CREATE TABLE messages(id INTEGER PRIMARY KEY AUTOINCREMENT, message TEXT ,
      fileName TEXT , fileContent BLOB , type INTEGER NOT NULL CHECK (type IN (1,2,3)),
      origin INTEGER NOT NULL CHECK (type IN (1,2)), contactId INTEGER,
      FOREIGN KEY (contactId) REFERENCES contacts(id));
EOF