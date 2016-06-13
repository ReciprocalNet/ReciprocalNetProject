#!/bin/sh
# Syntax: unrpm <file.rpm> <target directory>

cd $2
rpm2cpio $1 | cpio --extract --make-directories --quiet
