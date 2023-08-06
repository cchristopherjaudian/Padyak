import express from "express";
import userControllers from "../controllers/user-controller";
import requestSchemaValidate from "../lib/middlewares/joi-middleware";
import { createUserSchema } from "../lib/joi-schemas/cyclist-schema";

const router = express.Router();

router.post(
  "/",
  [requestSchemaValidate(createUserSchema)],
  userControllers.createUser
);

export default router;
