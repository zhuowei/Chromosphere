#!/bin/sh
. ./classpath.sh
rm -r bin
mkdir bin
javac -d bin -cp src:$MYCLASSPATH `find src -name "*.java" -printf "%p "`
