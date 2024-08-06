#!/bin/bash

User_Email="$1"

patient_data=$(grep "$User_Email" user-store.txt)

# shellcheck disable=SC2034
IFS=',' read -r email password UUID firstname lastname DoB CountryCode hasHIV DiagnosisDate OnART ARTStart Role LifeSpan<<< "$patient_data"

isHIVPositive=""

if [ "$hasHIV" = false ] ; then
	isHIVPositive="Negative"
	DiagnosisDate="None"
	OnART="None"
	ARTStart="None"
fi

if [ "$hasHIV" = true ] ; then
        isHIVPositive="Positive"
fi

patient_info="""
Profile Information for $firstname $lastname:
*********************************************

First Name: $firstname
Last Name: $lastname
Date of Birth: $DoB
Country Code: $CountryCode
HIV Status: $isHIVPositive
Diagnosis Date: $DiagnosisDate
ART Status: $OnART
ART Start: $ARTStart
User Role: $Role
Your Life Expectancy: $LifeSpan"Years"
"""

echo "$patient_info";

