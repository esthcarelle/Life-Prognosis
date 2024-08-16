#!/bin/bash
if [ "$#" -ne 13 ]; then
    echo "Usage: $0 <password>,<uuid>,<firstname>,<lastname>,<dOB>,<countryIsoCode>,<hasHIV>,<DiagnosisDate>,<OnART>,<ARTStart>,<role>,<lifeSpan>"
    exit 1
fi

USER_STORE="user-store.txt"
file_path="/mnt/c/AMANYA/CMU-Africa/Programming Bootcamp/TestProj/src/user-store.txt"

email="$1"
password="$2"
uuid="$3"
firstname="$4"
lastname="$5"
dOB="$6"
countryIsoCode="$7"
hasHIV="$8"
DiagnosisDate="$9"
OnART="${10}"
ARTStart="${11}"
role="${12}"
lifeSpan="${13}"

new_entry="$email,$password,$uuid,$firstname,$lastname,$dOB,$countryIsoCode,$hasHIV,$DiagnosisDate,$OnART,$ARTStart,$role,$lifeSpan"


#if grep -q "^$email," "$file_path"; then
#  sed -i "s|^$email,.*|$new_entry|" "$file_path"
if grep -q "^$email,.*,$uuid," "$USER_STORE"; then
  sed -i "s|^$email,.*,$uuid,.*|$new_entry|" "$USER_STORE"
  echo "Profile completed successfully."
else
  echo "User email and UUID do not exist."
fi

