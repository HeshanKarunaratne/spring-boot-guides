## GraphQL Queries

- Get all Person
~~~graphql
{
    getAllPerson{
        name
        address
        email
        mobile
    }
}
~~~

- Find by Email 
~~~graphql
{
    findPerson(email: "heshan@gmail.com"){
        name
        address
        email
        mobile
    }
}
~~~