### Employee repo implementation

Simple CRUD app for oparations with employees

#### Available endpoints
````
POST
http://localhost:8080/employees

GET
http://localhost:8080/employees/{id}

DELETE 
http://localhost:8080/employees/{id}

TODO PUT

````

#### Employee schema example

````
{
	"id": 200,
	"name":{"firstName":"Test Firstname","lastName":"Test Lastname"},
	"employeeType": {"employeeType":10}
}
````
