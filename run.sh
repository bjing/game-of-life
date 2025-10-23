#!/bin/sh

if [ "$#" -ne 1 ]; then
  echo "$0 \"initial-state\""
  echo "for example: ./run \"[[5, 5], [6, 5], [7, 5], [5, 6], [6, 6], [7, 6]]\""
  exit 1
fi

STATE=$1

sbt "run \"$STATE\""
