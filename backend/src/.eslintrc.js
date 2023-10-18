module.exports = {
    root: true,
    env: {
        es6: true,
        node: true,
    },
    extends: [
        'eslint:recommended',
        'plugin:import/errors',
        'plugin:import/warnings',
        'plugin:import/typescript',
        'google',
        'plugin:@typescript-eslint/recommended',
    ],
    parser: '@typescript-eslint/parser',
    parserOptions: {
        project: ['tsconfig.json', 'tsconfig.dev.json'],
        tsconfigRootDir: __dirname,
        sourceType: 'module',
    },
    ignorePatterns: [
        '/dist/**/*', // Ignore built files.
        '/node_modules/**/*',
    ],
    plugins: ['@typescript-eslint', 'import'],
    rules: {
        'quote-props': ['error', 'as-needed'],
        '@typescript-eslint/no-explicit-any': 'off',
        indent: 'off',
        'operator-linebreak': 'off',
        'import/no-unresolved': 0,
        'linebreak-style': 0,
        'require-jsdoc': [
            'error',
            {
                require: {
                    FunctionDeclaration: false,
                    MethodDefinition: false,
                    ClassDeclaration: false,
                    ArrowFunctionExpression: false,
                    FunctionExpression: false,
                },
            },
        ],
        'object-curly-spacing': 'off',
        '@typescript-eslint/no-non-null-assertion': 'off',
        'no-useless-catch': 'off',
        'new-cap': 'off',
    },
};
