import express from "express";
import { userController } from "../controllers";
import requestSchemaValidate from "../middlewares/joi-middleware";
import { createUserSchema } from "../lib/joi-schemas/cyclist-schema";
import TokenMiddleware from "../middlewares/token-middleware";

const router = express.Router();
const tokenMiddleware = new TokenMiddleware();

router.post(
  "/",
  [requestSchemaValidate(createUserSchema)],
  userController.createUser
);

router.post(
  "/test",
  [tokenMiddleware.validate as any, requestSchemaValidate(createUserSchema)],
  userController.testUser
);

export default router;
