#!/bin/sh

sudo semanage permissive -a cvs_t
sudo setsebool -P httpd_can_network_connect on
sudo yum install mariadb-server -y
sudo systemctl start mariadb
sudo mysql_secure_installation
sudo yum install  mysql-connector-java -y
sudo yum install cvs -y
sudo yum install httpd -y
sudo yum install mod_ssl -y
sudo yum install java-1.8.0-openjdk-devel -y
sudo alternatives --config javac
sudo alternatives --config java
sudo yum install wget
cd /usr/local
sudo wget http://download.nextag.com/apache/tomcat/tomcat-7/v7.0.70/bin/apache-tomcat-7.0.70.tar.gz
sudo tar xvfz apache-tomcat-7.0.70.tar.gz
sudo ln -s apache-tomcat-7.0.70/ tomcat
sudo useradd -s /sbin/nologin tomcat
sudo chown -R tomcat:tomcat /usr/local/tomcat/
cd
cd ReciprocalNetProject
sudo cp tomcat.service /etc/systemd/system/
sudo yum install ant -y
sudo yum install glibc.i686 -y
sudo yum install libsepol.i686 -y
sudo yum install libselinux.i686 -y
sudo yum install bzip2-devel -y
sudo yum install ant-apache-bcel -y
sudo yum install firewalld -y
sudo systemctl start firewalld
sudo systemctl enable firewalld
sudo firewall-cmd --zone=public --permanent --add-service=http
sudo firewall-cmd --zone=public --permanent --add-service=https
sudo firewall-cmd --reload
sudo firewall-cmd --zone=public --list-all
cd 
mkdir ReciprocalNetProject/SOURCES
cd ReciprocalNetProject/BUILD/RecipNet-SourceCode
tar -cvf recipnet-snapshot-HEAD-0376-20090321-source.tgz .
sudo mv recipnet-snapshot-HEAD-0376-20090321-source.tgz $HOME/ReciprocalNetProject/SOURCES
cd
cd ReciprocalNetProject
rpmbuild --define "_topdir $HOME/ReciprocalNetProject/" -ba SPECS/recipnet-site.spec
cd RPMS/x86_64
sudo rpm -ivh --nodeps RecipNet-server-SourceCode-50.x86_64.rpm
sudo rpm -ivh --nodeps RecipNet-webapp-SourceCode-50.x86_64.rpm
sudo rpm -ivh --nodeps RecipNet-utils-SourceCode-50.x86_64.rpm
sudo rpm -ivh --nodeps RecipNet-debuginfo-SourceCode-50.x86_64.rpm
cd
cd ReciprocalNetProject/BUILD/RecipNet-SourceCode
mkdir coordinator
mkdir coordinator/conf
mkdir coordinator/msgs-sent
sudo sh tools/coordinator.sh CreateCoordinatorSiteGrant
