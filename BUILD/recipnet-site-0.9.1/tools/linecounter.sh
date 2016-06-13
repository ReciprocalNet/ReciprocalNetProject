#!/bin/sh
#
# Reciprocal Net Project
#
# 2004-Feb-18: jobollin created history comment
# 2004-Feb-18: jobollin fixed bug #1121
# 2004-Feb-18: jobollin refactored the count*Directory functions by creating
#              a new function accumulateResults, removed the directory changing
#              that the script used to perform, switched many operations from
#              external programs (sed, expr, cut) to shell builtins, implemented
#              clean support for multiple directories of the various types, and
#              moved all configurable parameters to the top of the file
# 2005-Jul-12: ekoperda made directory listings sorted by name

SCLC=build/compile-time-tools/sclc/sclc
JAVASRCDIRS="javasrc"
NATIVEDIRS="native"
OTHERDIRS="tools"

function displayLeftPaddedString {
    echo -n "$1"
    for i in `seq ${#1} $2`; do
        echo -n " "
    done
}

function displayRightPaddedString {
    for i in `seq ${#1} $2`; do
        echo -n " "
    done
    echo -n "$1"
}

function countJavaPackage {
    local FILESEXIST=`find $1 -maxdepth 1 -name *.java`
    if [ -z "$FILESEXIST" ]; then
        # No java files in this directory.
        return;
    fi

    local DIRECTORY=${1:$((${#2} + 1))}
    local PACKAGENAME=${DIRECTORY//\//.}

    local RESULT=`$SCLC $1/*.java | tail -n 1`

    accumulateResults "$PACKAGENAME" "$RESULT"
}

function countNativeDirectory {
    local FILESEXIST=`find $1 -type f -maxdepth 1 \( -name *.c -or -name *.h -or -name *.cpp -or -name *.cgi -or -not -regex ".*\..*" \) -and -not -name CVS`
    if [ -z "$FILESEXIST" ]; then
        # No java files in this directory.
        return;
    fi

    local RESULT=`$SCLC $1/* -name .\*\\\\.c -name .\*\\\\.h -name .\*\\\\.cpp -name .\*\\\\.cgi -name "[^\.]*" -except CVS | tail -n 1`

    accumulateResults "$1" "$RESULT"
}

function countOtherDirectory {
    local RESULT=`$SCLC $1 -recurse -ignore | tail -n 1`
    accumulateResults "$1" "$RESULT"
}

function accumulateResults {
    local COUNT_LINES=${2:0:5}
    local COUNT_COMMENTS=${2:12:7}
    local COUNT_NCSL=${2:19:7}

    local PRETTY_COUNT_LINES=$(( ${COUNT_LINES} ))
    local PERCENT_COMMENTS=$(( ${COUNT_COMMENTS} * 100 / ${COUNT_LINES} ))
    local PERCENT_NCSL=$(( ${COUNT_NCSL} * 100 / ${COUNT_LINES} ))
    COUNT_TOTAL_LINES=$(( $COUNT_TOTAL_LINES + $COUNT_LINES ))
    COUNT_TOTAL_COMMENTS=$(( $COUNT_TOTAL_COMMENTS + $COUNT_COMMENTS ))
    COUNT_TOTAL_NCSL=$(( $COUNT_TOTAL_NCSL + $COUNT_NCSL ))

    displayLeftPaddedString "${1}:" 40
    displayRightPaddedString $PRETTY_COUNT_LINES 6
    echo -n " lines (NCSL ="
    displayRightPaddedString $PERCENT_NCSL 2
    echo -n "%, comments ="
    displayRightPaddedString $PERCENT_COMMENTS 2
    echo "%)"
}


# Initialize variables
let COUNT_TOTAL_LINES=0
let COUNT_TOTAL_COMMENTS=0
let COUNT_TOTAL_NCSL=0

# Count all our source files
for JAVASRCDIR in $JAVASRCDIRS; do
    for DIRNAME in `find $JAVASRCDIR -type d ! -name CVS | sort`; do
        countJavaPackage $DIRNAME $JAVASRCDIR
    done
done

for NATIVEDIR in $NATIVEDIRS; do
    for DIRNAME in `find $NATIVEDIR -type d ! -name CVS | sort`; do
        countNativeDirectory $DIRNAME
    done
done

for OTHERDIR in $OTHERDIRS; do
    countOtherDirectory $OTHERDIR 
done

# Display totals
echo ""
displayLeftPaddedString "TOTAL:" 40
displayRightPaddedString $COUNT_TOTAL_LINES 6
echo " lines, NCSL=$COUNT_TOTAL_NCSL, comments=$COUNT_TOTAL_COMMENTS"
