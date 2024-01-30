# restaurant-app
Restaurant saas toy
- mysql
  * build: docker build -t radb .
  * run: docker run -d -p 3333:3306 --name radb -e MYSQL_ROOT_PASSWORD=root -v $(pwd)/.db/mysql:/var/lib/mysql radb

# Version 1
- init spring boot
- create order api
- get order api