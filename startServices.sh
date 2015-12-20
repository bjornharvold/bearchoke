#!/bin/bash

echo "Starting Elasticsearch, MongoDb, RabbitMQ and Redis. You might have to configure this script to work with your local environment"

./start-rabbitmq.sh &
./start-redis.sh &
./start-mongodb.sh &
./start-elasticsearch.sh &