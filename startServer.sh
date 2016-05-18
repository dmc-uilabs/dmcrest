#!/bin/bash

mvn package -P swagger
sudo mv target/dmc-site-services-0.1.0-swagger.war rest.war
sudo mv rest.war /var/lib/tomcat7/webapps/rest.war

