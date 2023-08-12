import express from "express";
import { eventController } from "../controllers";
import requestSchemaValidate from "../middlewares/joi-middleware";
import TokenMiddleware from "../middlewares/token-middleware";
import {
  createEventSchema,
  getYearlyEventSchema,
  registerEventSchema,
  updateEventSchema,
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

router.get(
  "/:eventId",
  [tokenMiddleware.adminValidate as any],
  eventController.getEvent
);

router.patch(
  "/:eventId",
  [
    tokenMiddleware.adminValidate as any,
    requestSchemaValidate(updateEventSchema),
  ],
  eventController.updateEvent
);

router.patch(
  "/cyclist/:eventId",
  [
    tokenMiddleware.endUserValidate as any,
    requestSchemaValidate(registerEventSchema),
  ],
  eventController.registerEvent
);

export default router;
