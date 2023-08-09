import express from "express";
import { userController } from "../controllers";
import requestSchemaValidate from "../middlewares/joi-middleware";
import { createAdminSchema } from "../lib/joi-schemas/admin-schema";

const router = express.Router();

router.post(
  "/",
  [requestSchemaValidate(createAdminSchema)],
  userController.createUser
);

export default router;
