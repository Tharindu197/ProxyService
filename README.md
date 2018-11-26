# ProxyService
A RESTFull Web Service which acts like a proxy server. Caches API Responses for an hour inside a mongoDB with expiration state validation.

## Features:
* 3rd party external APIs can be configured with extending 'GenericEntity' abstract class and implementing own response entity (Deserializable to the output JSON value of API).
* Generalized Utility classes for external API calling (ApiCallProcessor) and validating expiration state (EntityValidator) of cached API response. [can be found in the utility package]

## Technologies used:
```
Spring-boot, JPA / Hibernate + MongoDB, Java Generics for abstraction and reusability
```
## Endpoint Summary:
```
[server]:[port]/proxy/weather?city=[city_id]
```
  ### Example
  ```
  http://localhost:8080/proxy/weather?city=524901
  ```
  
