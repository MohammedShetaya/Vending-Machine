# VendingMachine
In this project we simulate a vending machine functionality through an API. We used JWT token authentication where the user can obtain a token by posting a request to /auth/login with the body containing the username and password. The user has two roles, SELLER or BUYER. The user role determines the user authority on different recourses of the applicaiton.


## Installation

NOTE: to run this project, you have to have Docker installed on your machine. 

#### 1. In the root directory of the project, run the docker compose file to install the postgres image

```
docker-compose up -d

```

#### 2. Create the database vmachine

attach your terminal to the docker container running postgres
```
docker exec -it <container_id> /bin/bash

```

connect to postgres utility
```
psql -h localhost -p 5432 -U vmachine -d vmachine

```

create the database
```
create database vmachine;

```


#### 3. The applicaiton will create the tables upon starting the project





## End points


1. The user can buy a product as long as there is enough stock and the user deposit is larger than the total price

```
POST /user/buy

Authorization Bearer fgkoegnoirengoirengioengionegiokner

{
   "productId": 1,
   "amount": 2
}

```

2. The user can deposit a coin 

```
POST /user/deposit

Authorization Bearer fgkoegnoirengoirengioengionegiokner

{
   "coin": 10
}

```

3. The user can get return his coins

```
POST /user/reset

Authorization Bearer fgkoegnoirengoirengioengionegiokner

{

}

```

4. creating a new user

```
POST /user

{
   "username": "name",
   "password": "111",
   "role": "BUYER"
}


```


5. creating a new product

```
POST /product

{
   "productName": "fffffff",
   "amountAvailable": 15,
    "cost": 2,
    "seller": {
         "id": 1
    }
}


```

##### Other endpoints are simple CRUD operations for user(path= /user) and product (path= /product) models. 



