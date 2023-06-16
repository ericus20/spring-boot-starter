#! /bin/bash
./wait-for-it.sh postgres-db:5434 -t 60
java -Djava.security.egd=file:/dev/./urandom -cp .:lib/* com.developersboard.SpringBootStarterApplication
