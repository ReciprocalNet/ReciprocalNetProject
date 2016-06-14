#!/bin/sh
#
# 04-Feb-2005: ekoperda wrote first draft
#
# Syntax: unpackjava <binfile> <target directory>

cd $2
export MORE=10000
chmod +x $1
sh $1 <<EOF > /dev/null
yes
EOF
