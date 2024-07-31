#!/bin/bash

case "$1" in
  createCSV)
    touch users.csv analytics.csv
    echo "Created empty CSV files."
    ;;
  *)
    echo "Unknown command"
    ;;
esac
