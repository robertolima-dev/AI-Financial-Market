const express = require('express');
const morgan = require('morgan');
const path = require('path');

const app = express();

// Setup logger
app.use(morgan(':remote-addr - :remote-user [:date[clf]] ":method :url HTTP/:http-version" :status :res[content-length] :response-time ms'));

// Serve static assets
app.use(express.static(path.resolve(__dirname, '..', 'src/static')));

// Always return the main index_prod.html, so react-router render the route in the client
const indexFile = (process.env.NODE_ENV === 'production') ? 'index_prod.html' : 'index_dev.html'
app.get('*', (req, res) => {
    res.sendFile(path.resolve(__dirname,'..', indexFile));
});

module.exports = app;