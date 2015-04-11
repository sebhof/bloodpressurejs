#!/bin/bash

# Installs the connection pool in glassfish
# for development only
#
# shofmann, 03.02.2015

# read the default properties
source ./config.sh

# start server
asadmin start-domain $DOMAIN

# create the connection pool
asadmin create-jdbc-connection-pool \
	--datasourceclassname org.postgresql.ds.PGSimpleDataSource \
	--restype javax.sql.DataSource \
	--property user=$DB_USER:password=$DB_PASS:url="jdbc\\:postgresql\\://localhost\\:5432/$DB_INSTANCE" $POOL_NAME

# do not restart, since classpath is not re-built
asadmin stop-domain $DOMAIN
sleep 5
asadmin start-domain $DOMAIN

# check pool
asadmin ping-connection-pool $POOL_NAME

# create data source
asadmin create-jdbc-resource --connectionpoolid $POOL_NAME $DS_NAME

# stop the server
asadmin stop-domain $DOMAIN