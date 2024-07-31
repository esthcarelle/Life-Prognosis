#!/bin/bash

if [ "$#" -ne 1 ]; then
    echo "Usage: $0 <password>"
    exit 1
fi

pass="$1"

# salt=$(openssl rand -base64 32) -> This generated the salt
salt="+YuyPmrTA2Q0IKtG/BBq+rfUshB2jgQ5NJkByJqxEI8="

# We then Hash the password with the generated salt using SHA-256 algorithm

hash=$(echo -n "$pass$salt" | openssl dgst -sha256 | cut -d ' ' -f 2)

# Print the hashed value
echo "$hash"