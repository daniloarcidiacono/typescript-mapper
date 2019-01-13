#!/usr/bin/env bash

cat > $1/tsconfig.json << EOL

{
  "compilerOptions": {
    /* Basic Options */
    "target": "es6",                          /* Specify ECMAScript target version: 'ES3' (default), 'ES5', 'ES2015', 'ES2016', 'ES2017','ES2018' or 'ESNEXT'. */
    "module": "commonjs",                     /* Specify module code generation: 'none', 'commonjs', 'amd', 'system', 'umd', 'es2015', or 'ESNext'. */
    "noEmit": true,                           /* Do not emit outputs. */
    "pretty": true,                           /* Stylize errors and messages using color and context (experimental). */

    /* Strict Type-Checking Options */
    "strict": true,                           /* Enable all strict type-checking options. */
    "esModuleInterop": true                   /* Enables emit interoperability between CommonJS and ES Modules via creation of namespace objects for all imports. Implies 'allowSyntheticDefaultImports'. */
  }
}

EOL

../../../node_modules/typescript/bin/tsc -p $1

