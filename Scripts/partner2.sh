#!/bin/sh
sudo mysql_secure_installation
sudo drecipnet init
sudo drecipnet sync
sudo /etc/init.d/recipnetd start
sudo systemctl start tomcat
sudo systemctl start httpd