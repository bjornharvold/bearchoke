#!/usr/bin/env bash

echo "Stopping RabbitMQ and Redis. You might have to configure this script to work with your local environment"

echo "Stopping RabbitMQ server..."
rabbitmqctl stop

sleep 2

echo "Stopping RabbitMQ's erts daemon..."
kill -9 $(ps aux | grep '[e]rts' | awk '{print $2}')

sleep 2

echo "Stopping Redis server..."
kill -9 $(ps aux | grep '[r]edis-server' | awk '{print $2}')

sleep 2

echo "Stopping MongoDb server..."
kill $(ps aux | grep '[m]ongod' | awk '{print $2}')

sleep 2

echo "Stopping Elasticsearch server..."
kill -9 $(ps aux | grep '[e]lasticsearch' | awk '{print $2}')