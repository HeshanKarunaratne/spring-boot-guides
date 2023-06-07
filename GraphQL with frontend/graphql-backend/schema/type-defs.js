const { gql } = require("apollo-server");

const typeDefs = gql`

    type User {
        name: String!
        username: String!
        age: Int!
        nationality: String!
    }

    type Query {
        users: User
    }

`;