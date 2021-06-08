Once all connectors are up and running. Login to the docker host where kafka, ES and connector containers are running. Assuming you have curl installed....

```
curl -H "Content-Type: application/json" -X POST -d '{  "name": "delhi",  "config": {    "connector.class":"io.confluent.connect.elasticsearch.ElasticsearchSinkConnector",    "tasks.max": "1",    "topics": "delhi",    "key.ignore":"true",    "schema.ignore": "true",    "connection.url": "http://elasticsearch:9200",    "type.name": "delhi"  }}' http://localhost:8083/connectors
curl -H "Content-Type: application/json" -X POST -d '{  "name": "maharashtra",  "config": {    "connector.class":"io.confluent.connect.elasticsearch.ElasticsearchSinkConnector",    "tasks.max": "1",    "topics": "maharashtra",    "key.ignore":"true",    "schema.ignore": "true",    "connection.url": "http://elasticsearch:9200",    "type.name": "maharashtra"  }}' http://localhost:8083/connectors
curl -H "Content-Type: application/json" -X POST -d '{  "name": "karnataka",  "config": {    "connector.class":"io.confluent.connect.elasticsearch.ElasticsearchSinkConnector",    "tasks.max": "1",    "topics": "karnataka",    "key.ignore":"true",    "schema.ignore": "true",    "connection.url": "http://elasticsearch:9200",    "type.name": "karnataka"  }}' http://localhost:8083/connectors
curl -H "Content-Type: application/json" -X POST -d '{  "name": "tamilnadu",  "config": {    "connector.class":"io.confluent.connect.elasticsearch.ElasticsearchSinkConnector",    "tasks.max": "1",    "topics": "tamilnadu",    "key.ignore":"true",    "schema.ignore": "true",    "connection.url": "http://elasticsearch:9200",    "type.name": "tamilnadu"  }}' http://localhost:8083/connectors
```

If you do not have curl installed, one can visit the online curl website https://reqbin.com/curl and execute the above commands.

> **Note** Change the localhost term at the end of these commands with the actual public ip of your docker host 

Further references
https://www.confluent.io/blog/kafka-elasticsearch-connector-tutorial/
https://docs.confluent.io/cloud/current/connectors/cc-elasticsearch-service-sink.html
https://logz.io/blog/elasticsearch-mapping/
