import express from "express";
import { userController } from "../controllers";
import requestSchemaValidate from "../middlewares/joi-middleware";
import {
  createUserSchema,
  getUserByEmailSchma,
  updateUserSchema,
} from "../lib/joi-schemas/cyclist-schema";
import TokenMiddleware from "../middlewares/token-middleware";

const tokenMiddleware = new TokenMiddleware();
const router = express.Router();

router.post(
  "/",
  [requestSchemaValidate(createUserSchema)],
  userController.createUser
);

router.get(
  "/email",
  [requestSchemaValidate(getUserByEmailSchma)],
  userController.getUserByEmail
);

router.patch(
  "/",
  [
    tokenMiddleware.endUserValidate as any,
    requestSchemaValidate(updateUserSchema),
  ],
  userController.updateUser
);

export default router;
