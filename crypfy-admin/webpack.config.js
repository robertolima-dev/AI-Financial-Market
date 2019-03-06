// webpack.config.js
const webpack = require('webpack');
const path = require('path');

module.exports = {
    entry: './renderer.js',
    output: {
        filename: './src/static/js/renderer-bundle.js'
    },
    module: {
        loaders: [{
            test: /\.(js)$/,
            exclude: /(node_modules|bower_components)/,
            loader: 'babel-loader',
            query: {
                cacheDirectory: 'babel_cache',
                presets: ['react', 'env']
            }
        }]
    },
    plugins: [
        new webpack.DefinePlugin({
            //'process.env.NODE_ENV': JSON.stringify(process.env.NODE_ENV)
            'process.env.NODE_ENV': JSON.stringify(process.env.NODE_ENV || 'development')
        })
    ]
};