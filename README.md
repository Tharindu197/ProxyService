# ProxyService
A RESTFull Web Service which acts like a proxy server. Caches API Responses for an hour inside a mongoDB with expiration state validation.

## Features:
* 3rd party external APIs can be configured with extending 'GenericEntity' abstract class and implementing own response entity (Deserializable to the output JSON value of API).
* Generalized design: makes development easier. The service can be expanded to support multiple external APIs within few minutes and minimal lines of code.
* Generalized Utility classes for external API calling (ApiCallProcessor) and validating expiration state (EntityValidator) of cached API response. [can be found in the utility package]

## Technologies used:
```
Spring-boot, JPA / Hibernate + MongoDB, Java Generics for abstraction and reusability
```
## Library Documentation:
### 1. GenericEntity [abstract class / @Entity from SpringFramework]:
Your main entity class should inherit from GenericEntity class. GenericEntity adds auto-generated id and time-stamp to your entities (for the purpose of expiration validation and representing your entities within the DB).
  <br>e.g:
  ```java
  @Embeddable
  @Entity
  @Inheritance(strategy = InheritanceType.SINGLE_TABLE)
  public class MainEntity extends GenericEntity {
    ...
  }
  ```
  *MainEntity can be any of the entity class. Only MainEntity and its hierachically below (embedded) entities are store in the DB.
  
### 2. WebProxyServiceHelper [abstract class]
Perfoms transactions to the DB and External endpoints in order to fetching, validating and synching data between DB and External endpoints.
All service implementations should inherit from WebProxyServiceHelper class.

```java
@Service
public class CustomServiceImpl extends WebProxyServiceHelper implements CustomService{
  ...
}
```
#### Method Usage of WebProxyServiceHelper:

<b>1. getData(String URL, Class ResponseClass, Class entityClass, String wrapperFieldName)</b>
<br>
Returns a list of all objects declared by <i>Class entityClass</i>
###### params:
<ul>
  <li><b>URL:</b> URL to the endpoint with all required URL parameters</li>
<li><b>responseClass:</b> Outer most model class to deserialize API JSON response</li>
<li><b>entityClass:</b> Main Entity class which starts to represents data inside DB</li>
<li><b>wrapperFieldName:</b> Field names mapping (seperated by 'dot' operator) from outer response class to Main Entity(DB Representation) class</li></ul>

e.g:
```java
List<ExampleMainEntity> em = (ExampleMainEntity) getData("http://server/endpoint", ExampleResponse.class, ExampleMainEntity.class, "data.results");
//data.results here is the wrapper field name which means ExampleMainEntity instance could be found by navigating through,
//ExampleResponse-> Data -> List<ExampleMainEntity> results
```
<br>
<b>2. getData(String URL, Class ResponseClass, Class entityClass, Object id, String idFieldName, String wrapperFieldName)</b>
  Returns an object declared by Class entityClass
<br>

###### params:
<ul>
  <li><b>URL:</b> URL to the endpoint with all required URL parameters</li>
<li><b>responseClass:</b> Outer most model class to deserialize API JSON response</li>
<li><b>entityClass:</b> Main Entity class which starts to represents data inside DB</li>
  <li><b>id:</b>Any unique attribute for the purpose of lookup records in the DB, filtering</li>
  <li><b>idFieldName:</b>The field name of the above unique attribute as declared in the entity</li>
<li><b>wrapperFieldName:</b> Field names mapping (seperated by 'dot' operator) from outer response class to Main Entity(DB Representation) class</li></ul>

e.g:
```java
ExampleMainEntity em = (ExampleMainEntity) getData(URL, ExampleResponse.class, ExampleMainEntity.class, 5453, "_id", "data.results");
//data.results here is the wrapper field name which means ExampleMainEntity instance could be found by navigating through,
//ExampleResponse-> Data -> List<ExampleMainEntity> results
```

### 3. ApiCallProcessor [Utility Class]
This Utility class is automatically accessed and used within WebProxyServiceHelper.class and is not required to be explicitly used. For the customization purposes, the <b>processApiCall(URL, responseType)</b> method is exposed.

#### Method Usage of ApiCallProcessor:

<b>1. processApiCall(String URL, Class responseType)</b>
<br>
Returns the Response object rereived from External API, declared by <i>Class responseType</i>
###### params:
<ul>
  <li><b>URL:</b> URL to the endpoint with all required URL parameters</li>
<li><b>responseClass:</b> Outer most model class to deserialize API JSON response</li>
</ul>

e.g:
```java
ExampleResponse response = ApiCallProcessor.processApiCall("http://server/endpoint", ExampleResponse.class)
```

### 4. EntityValidator [Utility Class]
This Utility class is automatically accessed and used within WebProxyServiceHelper.class and is not required to be explicitly used. For the customization purposes, the <b>isExpired(entity)</b> method is exposed. 

#### Method Usage of EntityValidator:

<b>1. isExpired(Entity entity)</b>
<br>
Validates the expiration state for a given object stored in the DB. Returns true if the object is expired. Expiration timeout for a given stored object is 3600ms (1 hour) [This value can be changed by accessing the utility class].
###### params:
<ul>
  <li><b>entity:</b>Stored object inside the DB</li>
</ul>

e.g:
```java
  ExampleEntity entity = ...
  if(isExpired(entity)){
    ...
  }
```

### 5. URLFormatter [Utility Class]
This Utility class can be used to format a URL with a given servername and request parameters / paths.
<br><ul>
  <li>Obtain Instance via <b>URLFormatFactory</b>:</li></ul>

```java
String serverUrl = "...";
URLFormatter urlFormatter = URLFormatFactory.buildFormatter(serverUrl);

//or
String serverUrl = "...";
URLFormatter urlFormatter = URLFormatFactory.getFormatter();
urlFormatter.setURL(serverUrl);
```

#### Method Usage of URLFormatter:

Constructor:<br>
<b>1. URLFormatter(String URL)</b>
<br>
Sets the server URL. The setURL() setter can also be used for this purpose.
###### params:
<ul>
  <li><b>URL:</b>Server URL</li>
</ul>

<b>2. void addRequestParam(String key, String value)</b>
<br>
Adds request parameters to the URL. (Get mappings)
###### params:
<ul>
  <li><b>key:</b>Parameter name</li>
   <li><b>value:</b>Parameter value</li>
</ul>

e.g:
```java
  urlFormatter.addRequestParam("id", 2454557);
```

<b>3. void addPath(String path)</b>
<br>
Add request paths to the URL.
###### params:
<ul>
  <li><b>path:</b>Request path</li>
</ul>

e.g:
```java
  new URLFormatter("api").addPath("story");
  //output: /api/story/
```

<b>4. String getURL()</b>
<br>
Returns the formatted URL.

e.g:
```java
  urlFormatter.getURL();
```

### 6. RequestParamNormalizer [Auto Executed Aspect]
This Aspect is automatically executed before any HTTP Request to the controllers. The Aspect is there to prevent any error from getting occured when the expected request parameters are not present in the location expected location.
The Aspect will simply search if there are request body parameters present and will load them into the relevant java arguments if the URL parameters are not present.
Though the URL parameters are present, if related parameter is found in the request body, parameter from request body will receive the priority to serve as the argument.
With this Aspect, developers do not need to care about end-user's choice of sending parameters.

<ul>
  <li>Works with any endpoint with any HTTP mapping.</li>
  <li>Best suited for POST mappings.</li>
</ul>

#### Usage:
<b>NOTE: No seperate implementations required. Any RestController will work.
But java arguments of all request parameters must use Wrapper data type instead of premitive type due to a Spring limitation.
Also consider, only JSON data from request body are loaded into parameters this way.</b>

```java
    @PostMapping("/weather")
    public Element getWeatherByPost(@RequestParam(value = "city", required = false) Integer cityID) {
        return webProxyService.getWeather(cityID);
    }
```

## Example Endpoint Summary:
### Weather by Citycode:
```
[server]:[port]/proxy/weather?city=[city_id]
```
  #### Example
  ```
  http://localhost:8080/proxy/weather?city=524901
  ```
  ### Marvel Stories:
```
[server]:[port]/proxy/marvel/stories
```
  #### Example
  ```
  http://localhost:8080/proxy/marvel/stories
  ```
