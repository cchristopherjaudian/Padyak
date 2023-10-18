import express from 'express';
import cors from 'cors';
import CloudFunctions, { THttpsFunction } from './lib/cloud-functions';
// import * as cf from 'firebase-functions/v2/https';
import routes from './routes/index';
import RouteMiddleware from './middlewares/route-middleware';

const app = express();
const errorMiddlewares = new RouteMiddleware();

app.use(cors());
app.use(routes);

// route middlewares e.g(not found, error handlers)
app.use(errorMiddlewares.notFound);
app.use(errorMiddlewares.errorResponse);

// Create and deploy your first functions
// https://firebase.google.com/docs/functions/get-started
export const v2 = new CloudFunctions()
    .withRuntime()
    .handlerV2(<THttpsFunction>(<unknown>app));
