#!/bin/bash

input_file="user-store.txt"
search_email="$1"

input_firstname="$2"
input_lastname="$3"
input_DoB="$4"
input_HIVStatus="$5"
input_DiagnosisDate="$6"
input_ARTStatus="$7"
input_ARTStart="$8"


user_data=$(grep "$search_email" user-store.txt)

IFS=',' read -r email password UUID firstname lastname DoB CountryCode HIVStatus DiagnosisDate ARTStatus ARTStart Role LifeSpan<<< "$user_data"

if [ -z "$user_data" ]; then
	echo "User associated with $search_email Not Found."
else
	new_email="$email"
	new_password="$password"
	new_UUID="$UUID"

	if [ -z "$input_firstname" ] ; then
	  new_firstname="$firstname"
	else
	  new_firstname="$input_firstname"
	fi

	if [ -z "$input_lastname" ] ; then
  	  new_lastname="$lastname"
  else
  	  new_lastname="$input_lastname"
  fi

	if [ -z "$input_DoB" ] ; then
  	new_DoB="$DoB"
  else
  	new_DoB="$input_DoB"
  fi

	new_CountryCode="$CountryCode"

	if [ -z "$input_HIVStatus" ] ; then
  	new_HIVStatus="$HIVStatus"
  else
  	new_HIVStatus="$input_HIVStatus"
  fi

  if [ -z "$input_DiagnosisDate" ] ; then
    	new_DiagnosisDate="$DiagnosisDate"
    else
    	new_DiagnosisDate="$input_DiagnosisDate"
  fi

  if [ -z "$input_ARTStatus" ] ; then
      new_ARTStatus="$ARTStatus"
    else
      new_ARTStatus="$input_ARTStatus"
  fi

  if [ -z "$input_ARTStart" ] ; then
      new_ARTStart="$ARTStart"
    else
      new_ARTStart="$input_ARTStart"
  fi

	new_Role="$Role"
	new_LifeSpan="$LifeSpan"

	new_string="$new_email,$new_password,$new_UUID,$new_firstname,$new_lastname,$new_DoB,$new_CountryCode,$new_HIVStatus,$new_DiagnosisDate,$new_ARTStatus,$new_ARTStart,$new_Role,$new_LifeSpan"
	sed -i "/$search_email/c\\$new_string" "$input_file"	
	echo "Profile updated successfully!!!"
fi

