# Statistics on IoT Schemata

Calculating statistics of IoT Device schemata from Dweet.io and Sparkfun.

## Build

```
mvn clean dependency:copy-dependencies package
```

## Dweet.io Data Type Classifier

```
cd target
java -cp iotdevices-0.0.1-SNAPSHOT.jar uk.ac.soton.ldanalytics.iotdevices.DweetClassifier
```

Dictionary used for keys can be found [here](https://github.com/eugenesiow/iotdevices/blob/master/src/main/java/uk/ac/soton/ldanalytics/iotdevices/DweetClassifier.java).
`classifier.txt` and `_keylist.txt` are produced.

## Cross-IoT Study

[Get Array of Things Data](https://github.com/eugenesiow/iotdevices/tree/master/array_of_things)
