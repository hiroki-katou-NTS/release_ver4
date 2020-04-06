const path = require('path'),
    webpack = require('webpack'),
    TSLintPlugin = require('tslint-webpack-plugin'),
    CopyWebpackPlugin = require('copy-webpack-plugin'),
    UglifyJsPlugin = require("uglifyjs-webpack-plugin"),
    MiniCssExtractPlugin = require("mini-css-extract-plugin"),
    PackageWarFile = require("./webpack.package.war.plugin.js"),
    OptimizeCSSAssetsPlugin = require("optimize-css-assets-webpack-plugin");

module.exports = (env) => {
    return [{
        devtool: env && env.prod ? undefined : "source-map",
        mode: env && env.prod ? 'production' : 'development',
        stats: {
            modules: false
        },
        resolveLoader: {
            alias: {
                "vue-md-loader": path.join(__dirname, "vue-md-loader.js"),
                "vue-css-loader": path.join(__dirname, "vue-css-loader.js"),
                "vue-view-loader": path.join(__dirname, "vue-view-loader.js")
            }
        },
        resolve: {
            extensions: ['.js', '.ts'],
            alias: {
                'vue$': 'vue/dist/vue.common.js',
                '@app': path.join(__dirname, 'ClientApp', '@app'),
                'views': path.join(__dirname, 'ClientApp', 'views'),
                '@views': path.join(__dirname, 'ClientApp', 'views')
            }
        },
        module: {
            rules: [
                {
                    test: /\.ts$/,
                    include: /ClientApp/,
                    loaders: ['ts-loader']
                },
                {
                    test: /\.md$/,
                    include: /ClientApp/,
                    loaders: ['raw-loader', 'vue-md-loader']
                },
                {
                    test: /.(vue|html)$/,
                    include: /(views|components)/,
                    loaders: ["vue-view-loader"]
                },
                {
                    test: /\.(c|sa|sc)ss$/,
                    include: /(views|components)/,
                    loaders: ['raw-loader', 'vue-css-loader', 'sass-loader']
                },
                {
                    test: /\.(c|sa|sc)ss$/,
                    include: /styles/,
                    loaders: [MiniCssExtractPlugin.loader, 'css-loader', 'sass-loader']
                },
                { test: /\.(png|jpg|jpeg|gif|svg)$/, loaders: ['url-loader?limit=100000'] }
            ]
        },
        entry: {
            bundles: [
                './ClientApp/boot.browser.ts',
                './ClientApp/styles/dashboard.scss'
            ]
        },
        output: {
            path: path.join(__dirname, 'wwwroot', 'nts.uk.mobile.web', 'dist'),
            publicPath: 'nts.uk.mobile.web/dist',
            filename: '[name].js'
        },
        optimization: {
            minimize: env && env.prod,
            splitChunks: {
                chunks: 'async',
                minSize: 30000,
                maxSize: 0,
                minChunks: 1,
                maxAsyncRequests: 5,
                maxInitialRequests: 3,
                automaticNameDelimiter: '~',
                name: true,
                cacheGroups: {
                    vendors: {
                        test: /[\\/]node_modules[\\/]/,
                        priority: -10
                    },
                    default: {
                        minChunks: 2,
                        priority: -20,
                        reuseExistingChunk: true
                    }
                }
            },
            minimizer: [
                /*new UglifyJsPlugin({
                cache: true,
                parallel: true,
                uglifyOptions: {
                    compress: true,
                    ecma: 6,
                    mangle: true,
                    output: {
                        comments: false
                    }
                },
                sourceMap: false
                }),*/
                new OptimizeCSSAssetsPlugin({
                    cssProcessor: require("cssnano"),
                    cssProcessorOptions: {
                        discardComments: {
                            removeAll: true,
                        },
                        // Run cssnano in safe mode to avoid
                        // potentially unsafe transformations.
                        safe: true,
                    },
                    canPrint: false
                })
            ]
        },
        plugins: [
            new CopyWebpackPlugin([
                { from: path.join(__dirname, 'ClientApp', 'index.html'), to: path.join(__dirname, 'wwwroot', 'index.html') },
                { from: path.join(__dirname, 'ClientApp', 'favicon.ico'), to: path.join(__dirname, 'wwwroot', 'nts.uk.mobile.web', 'dist', 'favicon.ico') }
            ]),
            new MiniCssExtractPlugin({
                // Options similar to the same options in webpackOptions.output
                // both options are optional
                filename: '[name].css',
                chunkFilename: '[id].css',

            }),
            new webpack.DllReferencePlugin({
                context: __dirname,
                manifest: require(path.join(__dirname, 'wwwroot', 'nts.uk.mobile.web', 'dist', 'vendor-manifest.json'))
            }),
            // Plugins that apply in development builds only
            /*new webpack.SourceMapDevToolPlugin({
                // Remove this line if you prefer inline source maps
                filename: '[file].map',
                // Point sourcemap entries to the original file locations on disk
                moduleFilenameTemplate: path.relative(path.join(__dirname, 'wwwroot', 'nts.uk.mobile.web', 'dist'), '[resourcePath]')
            }),*/
            new TSLintPlugin({
                waitForLinting: true,
                warningsAsError: true,
                files: ['./ClientApp/*.ts', './ClientApp/**/*.ts']
            }),
            new PackageWarFile({ prod: env && env.prod }),
            new webpack.DefinePlugin({
                'process.env': {
                    'API_URL': JSON.stringify(env && env.api),
                    'NODE_ENV': JSON.stringify('development')
                }
            }),
        ],
        devServer: {
            hot: true,
            contentBase: path.join(__dirname, 'wwwroot'),
            compress: true,
            port: 3000,
            host: '0.0.0.0',
            useLocalIp: true,
            watchOptions: {
                poll: true
            },
            watchContentBase: true,
            index: './index.html',
            historyApiFallback: true,
            openPage: './nts.uk.mobile.web/ccg/007/b'
        },
        node: {
            __filename: true,
            __dirname: true,
        }
    }];
};