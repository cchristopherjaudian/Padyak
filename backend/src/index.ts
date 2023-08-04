import functions from "./lib/cloud-functions";

// Create and deploy your first functions
// https://firebase.google.com/docs/functions/get-started

exports.helloWorld = functions.https.onRequest((req, res) => {
  res.send("test1234");
});
