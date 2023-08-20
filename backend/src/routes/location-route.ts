import express from "express";
import { locationController } from "../controllers";
import requestSchemaValidate from "../middlewares/joi-middleware";
import {
  createLocationsSchema,
  getLocationsSchema,
} from "../lib/joi-schemas/location-schema";
import TokenMiddleware from "../middlewares/token-middleware";

const router = express.Router();
const tokenMiddleware = new TokenMiddleware();
router.get(
  "/",
  [
    tokenMiddleware.endUserValidate as any,
    requestSchemaValidate(getLocationsSchema),
  ],
  locationController.getLocations
);

router.post(
  "/",
  [
    tokenMiddleware.adminValidate as any,
    requestSchemaValidate(createLocationsSchema),
  ],
  locationController.createLocations
);

export default router;
