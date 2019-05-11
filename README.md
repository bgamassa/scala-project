## Start the project

### The aggregator
This is the HTTP REST API that receives the data from all IoT devices and
stores it in a database. This API exposes 2 endpoints:
  - `POST /data` to push data in the database
  - `GET /data/all` returns the data stored. It taked the following optional parameters:
    * `minID`: Int ?= 0
        Minmal message ID to return
    * `from`: String ?= "%"
        Only return messages match this expression
    * `reverse`: Boolean ?= false
        Reverse the order of messages (before limiting)
    * `limit`: Int ?= 50
        Limit the number of messages to return
  - `GET /actors` returns all the data authors

This is how to start the aggregator API
```bash
cd aggrgator
sbt run
```

A build of the aggregator is currently *hosted in the Cloud* and uses an *SQL database*
to store the data. This is the default POST endpoint of the emulator:
`http://scala-aggregator-api.eu-gb.mybluemix.net/data/all`


### The emulator
This is a program to emulate the behavior of an imaginary IoT device the sends
out data every few seconds. The data is actually read from json or csv files in
the current directory.

This is how to start an emulator it takes the following optional arguments:
  - `-interval` The interval at wich the emulator POSTs messages
  - `-scenario` The path of the directory the csv/json files will be read
  - `-endpoint` The complete URL to post data to
Example:
```bash
cd emulator
sbt "run -interval 10 -scenario default -endpoint locahost:9000/data"
```

### The frontend
The front end uses *ScalaJS* to query data from the aggrgator. You can build the
javascript file using:
```bash
cd frontend
sbt fastOptJS
```
Then serve the html static file as is and the data will be displayed and
automatically updated or you can also visit this link: `https://bgamassa.github.io/scala-project/` which is the deployed version of the front-end.
