#!/bin/bash

USER_STORE="user-store.txt"

# Generate UUID
generate_uuid() {
    echo $(uuidgen)
}

# First check if email exists
email_exists() {
    local email=$1
    if grep -q "^$email," "$USER_STORE"; then
        return 0 # Email exists already
    else
        return 1 # Email does not exist
    fi
}

# Admin Initiates Registration
initiate_registration() {
    local email=$1

    if email_exists "$email"; then
        echo "Error: Email $email already exists. Registration not initiated."
        return
    fi

    local uuid=$(generate_uuid)
    echo "$email,,$uuid,,,,,,,,,\"patient\"," >> $USER_STORE
    echo "Registration initiated for $email. Use UUID: $uuid to complete registration."
}

# Main
case "$1" in
    initiate_registration)
        initiate_registration $2
        ;;
    *)
        echo "Usage: $0 {initiate_registration|complete_registration|login}"
        exit 1
        ;;
esac
