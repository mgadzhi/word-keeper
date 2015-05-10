#!/usr/bin/env bash

set -e
set -o pipefail

echo 'Setting up database...'
psql -f setup_db.sql
echo 'Done'
