#!/bin/bash
if [ "$#" -ne 1 ]; then
    echo "Usage: $0 <uuid>"
    exit 1
fi

USER_STORE="user-store.txt"
file_path="/mnt/c/AMANYA/CMU-Africa/Programming Bootcamp/TestProj/src/user-store.txt"

uuid="$1"

#if grep -q "^$email," "$file_path"; then
#  sed -i "s|^$email,.*|$new_entry|" "$file_path"
if grep -q ",,$uuid," "$USER_STORE"; then
  echo "Found"
else
  echo "UUID does not exist."
fi


