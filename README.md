# Drone API

This API allows you to add a new drone to the list of drones, add a medication and create operations for drones and medications.

## Create drone

The endpoint for adding a new drone is POST: `{host}/drone`.

### Request Body

```json
{
    "serialNumber": "11344246ee889910",
    "model": "HEAVYWEIGHT",
    "weightLimit": 350,
    "batteryCapacity": 100,
    "state": "IDLE"
}
```

### Successfully response

```json
{
  "code": 201,
  "message": "Resource created successfully",
  "data": {
    "id": 3,
    "serialNumber": "11344246ee889910",
    "model": "HEAVYWEIGHT",
    "weightLimit": 350.0,
    "batteryCapacity": 100,
    "state": "IDLE",
    "createdAt": "2023-04-24T22:01:40.382454"
  }
}
```

## Get a  drone

The endpoint for get a drone is GET: `{host}/drone/{drone_id}`.

```json
{
  "code": 200,
  "message": "Successfully query",
  "data": {
    "id": 1,
    "serialNumber": "11224677889910",
    "model": "HEAVYWEIGHT",
    "weightLimit": 450.0,
    "batteryCapacity": 100,
    "state": "IDLE",
    "createdAt": "2023-04-24T22:01:26"
  }
}
```

## Create medication

The endpoint for adding a new medication is POST: `{host}/medication`.

### Request Body

```json
{
  "name": "test name six",
  "weight": 220,
  "code": "eeee44433",
  "image": "test_url"
}
```

### Successfully response

```json
{
  "code": 201,
  "message": "Resource created successfully",
  "data": {
    "id": 6,
    "name": "test name six",
    "weight": 220.0,
    "code": "eeee44433",
    "image": "test_url",
    "createdAt": "2023-04-24T22:02:46.915267"
  }
}
```

## Create operation

The endpoint for adding a new operation is POST: `{host}/medication`.

### Request Body

```json
{
  "droneId": 3,
  "medicationIds": [1,2]
}
```

### Successfully response

```json
{
  "code": 200,
  "message": "Resource created successfully",
  "data": {
    "id": 7,
    "drone": {
      "id": 3,
      "serialNumber": "11344246ee889910",
      "model": "HEAVYWEIGHT",
      "weightLimit": 350.0,
      "batteryCapacity": 100,
      "state": "DELIVERING",
      "createdAt": "2023-04-24T22:01:40"
    },
    "medications": [
      {
        "id": 1,
        "name": "test name one",
        "weight": 210.0,
        "code": "3335",
        "image": "test_url",
        "createdAt": "2023-04-24T22:01:57"
      },
      {
        "id": 2,
        "name": "test name two",
        "weight": 110.0,
        "code": "3345",
        "image": "test_url",
        "createdAt": "2023-04-24T22:02:05"
      }
    ],
    "createdAt": "2023-04-24T22:04:36.388792",
    "terminatedAt": null
  }
}
```

## Finish operation

The endpoint for finish operation is PUT: `{host}/medication`.

### Request Body

```json
{
  "operationId": 6
}
```

### Successfully response

```json
{
  "code": 200,
  "message": "Resource successfully updated",
  "data": {
    "id": 6,
    "drone": {
      "id": 1,
      "serialNumber": "11224677889910",
      "model": "HEAVYWEIGHT",
      "weightLimit": 450.0,
      "batteryCapacity": 10,
      "state": "LOADING",
      "createdAt": "2023-04-24T22:01:26"
    },
    "medications": [
      {
        "id": 1,
        "name": "test name one",
        "weight": 210.0,
        "code": "3335",
        "image": "test_url",
        "createdAt": "2023-04-24T22:01:57"
      },
      {
        "id": 2,
        "name": "test name two",
        "weight": 110.0,
        "code": "3345",
        "image": "test_url",
        "createdAt": "2023-04-24T22:02:05"
      },
      {
        "id": 3,
        "name": "test name three",
        "weight": 40.0,
        "code": "332335",
        "image": "test_url",
        "createdAt": "2023-04-24T22:02:15"
      }
    ],
    "createdAt": "2023-04-24T22:04:06",
    "terminatedAt": "2023-04-24T22:04:11.703499"
  }
}
```

## Get medication on drone

The endpoint for get a drone is GET: `{host}/operation/medication/drone/{drone_id}`.

```json
{
  "code": 200,
  "message": "Successfully query",
  "data": {
    "drone": {
      "id": 3,
      "serialNumber": "11344246ee889910",
      "model": "HEAVYWEIGHT",
      "weightLimit": 350.0,
      "batteryCapacity": 100,
      "state": "DELIVERING",
      "createdAt": "2023-04-24T22:01:40"
    },
    "operationId": 7,
    "medications": [
      {
        "id": 1,
        "name": "test name one",
        "weight": 210.0,
        "code": "3335",
        "image": "test_url",
        "createdAt": "2023-04-24T22:01:57"
      },
      {
        "id": 2,
        "name": "test name two",
        "weight": 110.0,
        "code": "3345",
        "image": "test_url",
        "createdAt": "2023-04-24T22:02:05"
      }
    ]
  }
}
```