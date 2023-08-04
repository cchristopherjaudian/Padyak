import type { Response } from "firebase-functions/v1";
import type { Request } from "firebase-functions/v1/https";
import * as functions from "firebase-functions";

// Create and deploy your first functions
// https://firebase.google.com/docs/functions/get-started
export type TRequestHandler = (
  request: Request,
  response: Response
) => void | Promise<void>;

export default functions.region("asia-northeast1");
