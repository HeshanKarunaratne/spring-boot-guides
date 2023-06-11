import React, { useState } from "react";
import { useQuery, gql, useLazyQuery, useMutation } from "@apollo/client";

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

const GET_MOVIE_BY_NAME = gql`
    query GetMovieByName($name: String!){
        movie(name: $name){
            name
            yearOfPublication
        }
    }
`

const CREATE_USER_MUTATION = gql`
    mutation CreateUser($input: CreateUserInput!){
        createUser(input: $input){
            name
            id
        }
    }
`

function DisplayData() {
    const [movieSearched, setMovieSearched] = useState("");
    const { data, loading, error, refetch } = useQuery(QUERY_ALL_USERS);
    const { data: movieData } = useQuery(QUERY_ALL_MOVIES);
    const [fetchMovie, { data: movieSearchedData, error: movieError }] = useLazyQuery(GET_MOVIE_BY_NAME);

    const [name, setName] = useState("");
    const [username, setUsername] = useState("");
    const [age, setAge] = useState(0);
    const [nationality, setNationality] = useState("");

    const [createUser] = useMutation(CREATE_USER_MUTATION);

    if (loading) {
        return <h1>Data Loading</h1>
    }

    return <div>

        {/* Show All users */}
        {data && data.users.map(user => {
            return <div key={user.id}>
                <h1>Name: {user.name}</h1>
                <h1>Age: {user.age}</h1>
                <h1>Username: {user.username}</h1>
            </div>
        })}

        {/* Show all movies */}
        {/* {movieData && movieData.movies.map(movie => {
            return <div key={movie.name}>
                <h1>Name: {movie.name}</h1>
                <h1>Year Of Publication: {movie.yearOfPublication}</h1>
            </div>
        })} */}

        {/* Search a movie */}
        {/* <div>
            <input type="text" placeholder="Enter movie here" onChange={(event) => {
                setMovieSearched(event.target.value)
            }}></input>
            <button onClick={() => fetchMovie({
                variables: {
                    name: movieSearched
                }
            })} >Fetch Movie</button>
            <h1>{movieSearched}</h1>
            {
                movieSearchedData && <div>
                    <h1>Movie Name: {movieSearchedData.movie.name}</h1>
                    <h1>Publication Year: {movieSearchedData.movie.yearOfPublication}</h1>
                </div>
            }
            {movieError && <h1>There was an error fetching data</h1>}
        </div> */}

        <div>
            <input type="text" placeholder="Name..." onChange={(event) => {
                setName(event.target.value)
            }} />
            <input type="text" placeholder="Username..." onChange={(event) => {
                setUsername(event.target.value)
            }} />
            <input type="number" placeholder="Age..." onChange={(event) => {
                setAge(event.target.value)
            }} />
            <input type="text" placeholder="Nationality..." onChange={(event) => {
                setNationality(event.target.value.toUpperCase())
            }} />
            <button onClick={() => {
                createUser({
                    variables: {
                        input: {
                            name,
                            age: Number(age),
                            username,
                            nationality
                        }
                    }
                });

                refetch();
            }}>Create User</button>
        </div>

    </div>
}

export default DisplayData;