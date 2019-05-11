## Start the project

### The aggregator
This is the HTTP REST API that receives the data from all IoT devices and
stores it in a database. This API exposes 2 endpoints:
  - `POST /data` to push data in the database
  - `GET /data/all` returns the data stored. It taked the following optionale
  parameters:
    * minID: Int ?= 0             # Minmal message ID to return
    * from: String ?= "%"         # Only return messages match this expression
    * reverse: Boolean ?= false   # Reverse the order of messages (before limiting)
    * limit: Int ?= 50            # Limit the number of messages to return
  - `GET /actors` returns all the data authors

This is how to start the aggregator API
```bash
cd aggrgator
sbt run
```

### The emulator
This is a program to emulate the behavior of an imaginary IoT device the sends
out data every few seconds. The data is actually read from json or csv files in
the current directory.

This is how to start an emulator
```bash
sbt "run -interval 10 -scenario default -endpoint locahost:9000/data"
```

### The frontend
Will have to query the aggregator for data to display in a user friendly UI

