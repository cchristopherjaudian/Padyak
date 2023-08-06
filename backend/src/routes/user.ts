import express from "express";
import { createUser } from "../handler/user-handler";

const router = express.Router();

router.post("/", createUser);

export default router;
