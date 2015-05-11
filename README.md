# Unico-New
Unico Project required for assessment

## The project enables a user to calculate GCD of 2 given numbers.
### Technology Used
* Spring - REST
* Spring - WS
* JMS

### Configuration
Create 2 message queues on the application server with below names 
```sh
jboss/exported/jms/queue/gcd
```

```sh
jboss/exported/jms/topic/test`
```

### Usage
1. Push 2 numbers using GET method in queue

URL
```sh
http://localhost:8080/rest-web-app/rest/pushRequest
```
BODY
```sh
<push>
<i1>102</i1>
<i2>84</i2>
</push>
```
2. List all pushed numbers using GET method

URL
```sh
http://localhost:8080/rest-web-app/rest/list
```
```sh
<push>
<i1>102</i1>
<i2>84</i2>
</push>
```
3. To calculate GCD of the current pushed numbers call the below SOAP service
The WSDL of the SOAP service exposed is povided below. Use is to generate sample request for all the three services
```sh
http://localhost:8080/rest-web-app/spring-ws/login.wsdl
```
a. Calculate GCD of top 2 numbers in the queue and delete them from the queue and add the calculated GCD in another queue
```sh
* GCD
```
b. List all the calculated GCD's from the mesage queue
```sh
* GCDList
```
c. Calculate the sum of all GCD's
```sh
* GCDSum
```
