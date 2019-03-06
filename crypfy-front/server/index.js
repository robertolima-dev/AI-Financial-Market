// server/index.js
'use strict';

const app = require('./app');

const PORT = process.env.PORT || 9000;

app.listen(PORT, () => {
    console.log('App enviroment ' + process.env.NODE_ENV );
    console.log(`App listening on port ${PORT}!`);
});