#!/bin/sh
#
# Invoke this script from the source tree root, specifying two CVS sticky tags,
# older than newer.

javac tools/whatchanged.java
java -cp .:tools -Xmx512m whatchanged verbose /usr/bin/cvs recipnet $1 $2