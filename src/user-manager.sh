#!/bin/bash

USER_STORE="user-store.txt"

# Generate UUID
generate_uuid() {
    echo $(uuidgen)
}

# Admin Initiates Registration
initiate_registration() {
    local email=$1
    local uuid=$(generate_uuid)
    echo "$email:$uuid:ADMIN:" >> $USER_STORE
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
