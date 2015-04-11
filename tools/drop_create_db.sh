#!/bin/bash

# Drops and creates the database for development purposes only
#
# shofmann, 07.02.2015

# read the default properties
source ./config.sh

sudo -u postgres dropdb $DB_INSTANCE
sudo -u postgres dropuser $DB_USER

sudo -u postgres createuser -D -P -R -S $DB_USER
sudo -u postgres createdb -E UTF-8 -O $DB_USER $DB_INSTANCE
