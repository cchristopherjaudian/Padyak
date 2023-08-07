import express from "express";
import { userController } from "../controllers";
import requestSchemaValidate from "../middlewares/joi-middleware";
import { createUserSchema } from "../lib/joi-schemas/cyclist-schema";

const router = express.Router();

router.post(
  "/",
  [requestSchemaValidate(createUserSchema)],
  userController.createUser
);

export default router;
