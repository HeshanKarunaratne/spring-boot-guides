import React from "react";
import { useQuery, gql } from "@apollo/client";

const QUERY_ALL_USERS = gql`
    query GetAllUsers {
        users {
            id
            username     
            name
            age
        }
    }
`

const QUERY_ALL_MOVIES = gql`
    query GetMovies {
        movies {
            name
            yearOfPublication
        }
    }
`

function DisplayData() {

    const { data, loading, error } = useQuery(QUERY_ALL_USERS);
    const { data: movieData } = useQuery(QUERY_ALL_MOVIES);
    if (loading) {
        return <h1>Data Loading</h1>
    }

    if (error) {
        console.log(error);
    }

    return <div>
        {data && data.users.map(user => {
            return <div key={user.id}>
                <h1>Name: {user.name}</h1>
                <h1>Age: {user.age}</h1>
                <h1>Username: {user.username}</h1>
            </div>
        })}

        {movieData && movieData.movies.map(movie => {
            return <div key={movie.name}>
                <h1>Name: {movie.name}</h1>
                <h1>Year Of Publication: {movie.yearOfPublication}</h1>
            </div>
        })}
    </div>
}

export default DisplayData;