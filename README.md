# Route finder

## Description

This program is used to find shortest route between two countries. Countries are defined in this file:
 https://raw.githubusercontent.com/mledoze/countries/master/countries.json

## How to run
First it is necessary to build the application and create executable **jar** file:

```
go to the location where the pom.xml is
mvn package
```

We have successfully built the application. The executable application is located in target folder.
 
```
cd target
``` 

Now we can run the application:

```
java -jar pathfinder-1.0-SNAPSHOT.jar
```

The application is up and running now. It listens on port **8086**. We can test it:

```
open browser and navigate to http://localhost:8086/routing/CZE/ITA
```

As a result route from CZE to ITA is returned.