import express from "express";
import { alertController } from "../controllers";
import requestSchemaValidate from "../middlewares/joi-middleware";
import TokenMiddleware from "../middlewares/token-middleware";
import { sendAlertSchema } from "../lib/joi-schemas/alert-schema";

const router = express.Router();
const tokenMiddleware = new TokenMiddleware();

router.post(
  "/",
  [
    tokenMiddleware.endUserValidate as any,
    requestSchemaValidate(sendAlertSchema),
  ],
  alertController.sendAlert
);

export default router;
