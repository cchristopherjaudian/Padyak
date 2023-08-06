import express from "express";
import routes from "./routes/index";
import functions from "./lib/cloud-functions";

const app = express();

app.use(routes);

// Create and deploy your first functions
// https://firebase.google.com/docs/functions/get-started
exports.users = functions.https.onRequest(app);
