const { gql } = require("apollo-server");

const typeDefs = gql`

    type User {
        id: ID!
        name: String!
        username: String!
        age: Int!
        nationality: Nationality!
        friends: [User]
        favouriteMovies: [Movie]
    }

    type Query {
        users: UsersResult
        user(id: ID!): User!
        movies: [Movie!]!
        movie(name: String!): Movie!
    }

    input CreateUserInput {
        name: String!
        username: String!
        age: Int!
        nationality: Nationality = BRAZIL
    }

    input UpdateUsernameInput {
        id: ID! 
        newUsername: String!
    }

    type Mutation {
        createUser(input: CreateUserInput!): User
        updateUsername(input: UpdateUsernameInput!): User
        deleteUser(id: ID!): User
    }

    type Movie {
        id: ID!
        name: String!
        yearOfPublication: Int!
        isInTheaters: Boolean!
    }

    enum Nationality {
        CANADA
        BRAZIL
        INDIA
        GERMANY
        CHILE
    }

    type UsersSuccessfulResult {
        users: [User!]!
    }

    type UsersErrorResult {
        message: String!
    }

    union UsersResult = UsersSuccessfulResult | UsersErrorResult
`;

module.exports = { typeDefs };







// query Union{ 
//     users{
//       ...on UsersSuccessfulResult{
//         users {
//           id
//           name
//           age
//         }
//       }
  
//       ...on UsersErrorResult{
//         message
//       }
//     }
// }