#!/bin/bash

echo "Starting RabbitMQ and Redis. You might have to configure this script to work with your local environment"
echo "If you set your environment property RABBITMQ_HOME you should not have to edit these scripts"

./start-rabbitmq.sh &
./start-redis.sh &