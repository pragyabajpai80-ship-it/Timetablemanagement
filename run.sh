#!/bin/bash

if [ -z "$AIVEN_DB_PASSWORD" ]; then
    echo "AIVEN_DB_PASSWORD is not set."
    echo "Run: export AIVEN_DB_PASSWORD='YOUR_AIVEN_PASSWORD'"
    exit 1
fi

java -cp "bin:/usr/share/java/mariadb-java-client.jar" TimetableDashboard
