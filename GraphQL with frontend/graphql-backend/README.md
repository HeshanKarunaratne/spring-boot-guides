# GraphQL

- Creating graphQL backend using node
    1. Generate a npm project
        - npm init
    2. Add necessary packages
        - npm i apollo-server graphql

- Using Fragments
~~~js
query GetAllUsers {
    users {
      ...GetNameAndAge
    }
}
  
fragment GetNameAndAge on User {
    age
    name
}
~~~

- Using Union(Implemented code)
~~~js
query Union{ 
  users{
    ...on UsersSuccessfulResult{
      users {
        id
        name
        age
      }
    }

    ...on UsersErrorResult{
      message
    }
  }
}
~~~