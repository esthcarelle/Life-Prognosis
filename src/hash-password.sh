#!/bin/bash
if [ "$#" -ne 1 ]; then
    echo "Usage: $0 <password>"
    exit 1
fi

pass="$1"

# salt=$(openssl rand -base64 32)
salt="+YuyPmrTA2Q0IKtG/BBq+rfUshB2jgQ5NJkByJqxEI8="

# Hash the password with the generated salt using OpenSSL's SHA-256

hash=$(echo -n "$pass$salt" | openssl dgst -sha256 | cut -d ' ' -f 2)

# Print the hashed value
echo "$hash"