# Array of Things Data

Data from the AoT Sensor Network is retrieved via the Plenar.io open data portal.

## Get Metadata

```
./get_metadata.sh
```

## Get Data from past Week

```
curl -o data.csv http://plenar.io/v1/api/sensor-networks/array_of_things_chicago/download
```