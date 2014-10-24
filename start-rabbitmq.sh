#!/bin/bash

echo "RabbitMQ_HOME is $RABBITMQ_HOME"
echo "Starting RabbitMQ server..."
cd $RABBITMQ_HOME/sbin
./rabbitmq-server start