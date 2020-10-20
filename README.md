# Instagram

To build the project following requirements should be met:
* Installed Oracle JDK 8

Internally I use:
* In-memory h2 database for easy start 

To assemble JAR, in the project root directory run:
```bash
mvn clean package
```

Build generate following artifacts:
- executable jar file Instagram-1.0-SNAPSHOT.jar

Configuration: to set up your application you need to edit application.properties file
Following properties are mandatory: 
* imageServer.host - default value [http://interview.agileengine.com] - Original image server
* imageServer.authUrl - default value [/auth] - relative path to authorisation service
* imageServer.imagesUrl - default value [/images] - relative path to image API
* imageServer.apiKey  - default value [23567b218376f79d9415] - apiKey
* image.cacheFolder - default value [F:/Projects/Instagram/target/images] - path to local folder where images caching
* refreshTimeMilliseconds - default value [50000] - cache refresh time in milliseconds


To run execute 
```bash
java -jar target/Instagram-1.0-SNAPSHOT.jar
```


