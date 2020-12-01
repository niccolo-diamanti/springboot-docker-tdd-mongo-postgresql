# Springboot Backend example

##### SpringBoot application with PostgreSQL and MongoDB. 
* PostgreSQL is used to store `products` 
* MongoDB is used to store `bundles` and `cart` 

## Running Dependencies
* Docker

## Test Dependencies
* Java JDK 11
* Gradle
* Docker

## Init DB script
At start both MongoDB and PostgreSQL databases are initialized with provided data.
On the `import` folder there are `mongodb` folder with `init-mongo.js` that are used by Docker to initialize MongoDB container.
On the `postgres` folder we have `init.sql` that are used by Docker to initialize PostgreSQL container.

## Running tests
If you're using IDE like Intellij IDEA, open Gradle window and run "test" on the verification tasks.

If you want to run tests using the command line, use this command:

```
gradlew test
```

## Installing
To run the app you need only to run this command on the project directory:

```
gradle bootJar
docker-compose up -d --build
```

## API documentation
It's provided by local [Swagger](http://localhost:8080/api/swagger-ui.html)

# Examples
After docker deployment, you can go on the [Swagger](http://localhost:8080/api/swagger-ui.html) page that should looke like this. 
We can try API services on this page. 
### 1 - Create cart
Open the POST - /cart/v1/create tab and click "try it out". Then insert a json payload like this to create a cart with 1 product and click "Execute" button:
```
{
     "id": "c87aec75-5840-4d42-a561-945f09210467",
     "products": [
       "GJYLP-2203"
     ],
     "price": 96.34,
     "discount": 0
}
```
![image](https://user-images.githubusercontent.com/36787286/100683092-ea4d6e80-3377-11eb-80cb-d8783741670d.png)

### 2 - Update cart, add product
Open the PUT - /cart/v1/{cartId} tab and click "try it out". Then insert a json payload like this to add 1 product:
```
{
    "sku": "AQKQX-3571",
    "price": 79.9
}
```
On cartId you can insert previous cart id and click "Execute" button:
```
c87aec75-5840-4d42-a561-945f09210467
```
![image](https://user-images.githubusercontent.com/36787286/100683600-f38b0b00-3378-11eb-871d-acdfac5b2f8b.png)

You should get a response like this:
![image](https://user-images.githubusercontent.com/36787286/100683927-9e9bc480-3379-11eb-88b3-979ff040b4ef.png)

We can add more product with the same cart id, for example:
```
{
    "sku": "OVVUK-8951",
    "price": 36.86
}
```

### 3 - Get cart
Open the GET - /cart/v1/{cartId} tab and click "try it out". Then insert a cartId like we did before:
```
c87aec75-5840-4d42-a561-945f09210467
```
![image](https://user-images.githubusercontent.com/36787286/100684234-37324480-337a-11eb-8920-734a8c6fa4ac.png)

The response should look like:
```
{
  "id": "c87aec75-5840-4d42-a561-945f09210467",
  "products": [
    "GJYLP-2203",
    "AQKQX-3571",
    "OVVUK-8951"
  ],
  "price": 213.10000000000002,
  "discount": 88.12
}
```

## Compromises/shortcuts
* No security
* Default logging
* Default Swagger
* Only 1 product at time can be added
* Only 1 product of the same time can be added