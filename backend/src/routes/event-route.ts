import express from "express";
import { eventController } from "../controllers";
import requestSchemaValidate from "../middlewares/joi-middleware";
import TokenMiddleware from "../middlewares/token-middleware";
import {
  createEventSchema,
  getYearlyEventSchema,
} from "../lib/joi-schemas/event-schema";

const router = express.Router();
const tokenMiddleware = new TokenMiddleware();

router.post(
  "/",
  [
    tokenMiddleware.adminValidate as any,
    requestSchemaValidate(createEventSchema),
  ],
  eventController.createEvent
);

router.get(
  "/count",
  [
    tokenMiddleware.adminValidate as any,
    requestSchemaValidate(getYearlyEventSchema),
  ],
  eventController.getYearlyEvents
);

export default router;
