import express from "express";
import routes from "./routes/index";
import functions from "./lib/cloud-functions";
import RouteMiddleware from "./middlewares/route-middleware";

const app = express();
const errorMiddlewares = new RouteMiddleware();

app.use(routes);

// route middlewares e.g(not found, error handlers)
app.use(errorMiddlewares.notFound);
app.use(errorMiddlewares.errorResponse);

// Create and deploy your first functions
// https://firebase.google.com/docs/functions/get-started
exports.padyak = functions.https.onRequest(app);
