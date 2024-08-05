#!/bin/bash

input_email="$1"
input_password="$2"
input_file="user-store.txt"


user_data=$(grep "$input_email" user-store.txt)

IFS=',' read -r email password UUID firstname lastname DoB CountryCode hasHIV DiagnosisDate OnART ARTStart Role LifeSpan <<< "$user_data"

if [ -z "$input_email" ] || [ -z "$input_password" ]  ; then
	echo "Email and Password are required"
else

if [ -z "$user_data" ]; then
	echo "User associated with $input_email Not Found."
elif [ "$input_password" != "$password" ]; then
	echo "Invalid Password."
else
	FILE="LoggedIn_Users_Session_File.txt"
	VALUES="$input_email,$Role,LoggedIn"

	# Check if the file exists
	if [ ! -e "$FILE" ]; then
  		# Create the file
  		touch "$FILE"
		#Append needed values to created file
		echo "$VALUES" >> "$FILE"
	else
		echo "$VALUES" >> "$FILE"
	fi

	echo "Welcome, you will be viewing your dashboard shortly..."
fi
fi