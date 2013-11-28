# Load-test-target App

A load test target application. The root path for this application is:

```
http://yourserver.com/loadtest
```



## Services

### - Predictable Service

**Path:**

```
predictable
```

**Description:**

Provides predictable responses in terms of size and time to return (server-side time only, network time cannot be predicted).

**Accepts**

 - **application/json**
 - **application/xml** *(default)*

**REST Resources:**

 - **small** - Xml response of 789 bytes **OR** Json response of 583 bytes
 - **medium** - Xml response of 5028 bytes **OR** Json response of 3462 bytes
 - **large** - Xml response of 104348 bytes **OR** Json response of 52483 bytes

Example:
```
http://server.com/loadtest/predictable/small
```

**Queries**

 - delay - an integer value

Examples:
```
http://server.com/loadtest/predictable/large?delay=500
http://server.com/loadtest/predictable/?delay=250
```
 
## Deploying this web app

Just build with Maven:

```
mvn clean install
```

This creates a WAR file in the `target` folder. This file can be deployed in any Servlet Container like Jetty or Tomcat.

In the case of Tomcat, just drop the WAR into the Tomcat's `webapps` folder and Tomcat will get it started immediately.
