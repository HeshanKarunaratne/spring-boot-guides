const { UserList, MovieList } = require('../FakeData');
const _ = require('lodash');
const resolvers = {
    Query: {
        users: () => {
            return UserList;
        },

        user: (parent, args) => {
            const id = args.id;
            return _.find(UserList, { id: Number(id) });
        },

        movies: () => {
            return MovieList;
        },

        movie: (parent, args) => {
            const name = args.name;
            return _.find(MovieList, { name });
        },
    }
}

module.exports = { resolvers };