#!/usr/bin/env bash

echo "Stopping RabbitMQ and Redis. You might have to configure this script to work with your local environment"
echo "If you set your environment property RABBITMQ_HOME you should not have to edit these scripts"

echo "RabbitMQ_HOME is $RABBITMQ_HOME"
echo "Stopping RabbitMQ server..."
cd $RABBITMQ_HOME/sbin
./rabbitmqctl stop

sleep 3

cd ../erts-5.10.3/bin
./epmd -kill

echo "Stopping Redis server..."
kill -9 $(ps aux | grep '[r]edis-server' | awk '{print $2}')