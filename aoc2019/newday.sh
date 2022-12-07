#!/usr/bin/env bash
cd ~/git/adventofcode/2019/

if [ -n "$1" ] ; then
    day="$1"
else
    echo -n "Enter day: "
    read day
fi

folder="day"$day

mkdir $folder

file="Day"$day

cp DayX.java $folder/$file.java

sed -i -e 's/DayX/'$file'/g' $folder/$file.java

rm -f $folder/$file.java-e

git add $folder/*
git commit -m "init "$folder
