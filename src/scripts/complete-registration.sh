#!/bin/bash
if [ "$#" -ne 13 ]; then
    echo "Usage: $0 <email>,<password>,<uuid>,<firstname>,<lastname>,<dOB>,<countryIsoCode>,<hasHIV>,<DiagnosisDate>,<OnART>,<ARTStart>,<role>,<lifeSpan>"
    exit 1
fi

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


if grep -q "^$email," "$file_path"; then
  sed -i "s|^$email,.*|$new_entry|" "$file_path"
  echo "User details updated successfully."
else
  echo "User email does not exist."
fi

