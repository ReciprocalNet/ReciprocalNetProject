#!/bin/sh
#
# 07-May-2008: ekoperda wrote first draft
#
# Syntax: coordinator <nameofclass>
# Example: coordinator DumpMsgs
#
# Invoke this program from the base of a checked-out copy of the Reciprocal Net
# source tree.  Be sure this directory contains a symlink named "coordinator" 
# that points to the actual coordinator data files for the particular Site
# Network you wish to control.


ant javasrc
if [ $? -ne 0 ]; then
    echo "Run script aborted!"
    cd ..
    exit 1
fi
echo ""
echo "Finished compiling."
echo ""

BASEDIR=`pwd`
cd coordinator

CLASSNAME=org.recipnet.coordinator.$1
shift
java -classpath ${BASEDIR}/build/classes/ $CLASSNAME $@

