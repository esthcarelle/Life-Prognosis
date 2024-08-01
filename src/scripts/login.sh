#!/bin/bash

input_email="$1"
input_password="$2"

user_data=$(grep "$input_email" user-store.txt)

IFS=',' read -r email password UUID <<< "$user_data"

if [ -z "$input_email" ] || [ -z "$input_password" ]  ; then
	echo "Email and Password are required"
else

if [ -z "$user_data" ]; then
	echo "User associated with $input_email Not Found."
elif [ "$input_password" != "$password" ]; then
	echo "Invalid Password."
else
	echo "Welcome, you will be viewing your dashboard shortly your dashboard..."
fi
fi