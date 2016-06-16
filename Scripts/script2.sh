#!/bin/sh
cd
cd ReciprocalNetProject/BUILD/RecipNet-SourceCode
sudo sh tools/coordinator.sh CreateSiteGrant
sudo cp coordinator/recipnet.sitegrant /etc/recipnet/
sudo mv coordinator/recipnet.sitegrant coordinator/recipnet.sitegrant.site1
sudo mysql_secure_installation
sudo drecipnet init
sudo /etc/init.d/recipnetd start
sudo systemctl start tomcat
sudo systemctl start httpd