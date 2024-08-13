#!/bin/bash
if [ "$#" -ne 1 ]; then
    echo "Usage: $0 <Country ISO Code>"
    exit 1
fi

code="$1"

EXPECTANCY_FILE="life-expectancy.csv"

# Use grep to find the line with the given code and extract the life expectancy
life_expectancy=$(grep ",$code," "$EXPECTANCY_FILE" | cut -d',' -f7)

# Check if life expectancy is found
if [ -z "$life_expectancy" ]; then
    echo "No life expectancy found for: $code"
else
    echo "$life_expectancy"
fi