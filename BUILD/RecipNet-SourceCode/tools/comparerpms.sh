#!/bin/sh
#
# Compares the contents of one RPM to the contents of another, returning a
# nonzero value only if there are substantive differences between them.
# Ignores some specific minor differences like build numbers, etc.
#
# Usage:
#   comparerpms.sh <rpm1> <rpm2>
#
# Exit codes:
#   0: no substantive differences
#   1: substantive differences
#   2: substantive differences in binary files, or error computing diff
#
# Revision history:
#    12-Feb-2004: ekoperda wrote first draft

RPM1=$1
RPM2=$2
TEMPDIR1=`mktemp -d /tmp/compare-rpms.XXXXXX`
TEMPDIR2=`mktemp -d /tmp/compare-rpms.XXXXXX`

# Populate the temporary directories with the contents of our RPMS.
cd $TEMPDIR1
rpm2cpio $RPM1 | cpio --extract --make-directories --quiet
cd $TEMPDIR2
rpm2cpio $RPM2 | cpio --extract --make-directories --quiet

# Special handling for .jar files: uncompress/extract them so that their 
# contents can be checked separately.
mkdir $TEMPDIR1/.jar-contents
cd $TEMPDIR1/.jar-contents
find .. -name *.jar -exec jar -xf {} \;
mkdir $TEMPDIR2/.jar-contents
cd $TEMPDIR2/.jar-contents
find .. -name *.jar -exec jar -xf {} \;

# Special handling for .gz files: uncompress/extract them so that their
# contents can be checked separately.
find $TEMPDIR1 -name *.gz -exec gunzip {} \;
find $TEMPDIR2 -name *.gz -exec gunzip {} \;

# Run diff to detect all the differences.  Ignore .jar files.  Also ignore
# certain build-dependent classes.
echo "Comparing $RPM1 against $RPM2..."
diff --brief --recursive \
     --exclude *.jar \
     --exclude *.gz \
     --exclude VersionUpdater.class \
     --exclude footer.jsp \
     --exclude mod_recipnet_auth-*.so \
     $TEMPDIR1 \
     $TEMPDIR2
EXITCODE=$?

# Clean up.
rm -rf $TEMPDIR1 $TEMPDIR2

exit $EXITCODE
