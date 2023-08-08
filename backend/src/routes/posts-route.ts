import express from "express";
import { postController } from "../controllers";
import requestSchemaValidate from "../middlewares/joi-middleware";
import TokenMiddleware from "../middlewares/token-middleware";
import {
  addCommentSchema,
  addLikesSchema,
  createPostSchema,
  updatePostSchema,
} from "../lib/joi-schemas/posts-schema";

const router = express.Router();
const tokenMiddleware = new TokenMiddleware();

router.post(
  "/",
  [tokenMiddleware.validate as any, requestSchemaValidate(createPostSchema)],
  postController.createPost
);

router.get("/", [tokenMiddleware.validate as any], postController.getPosts);

router.patch(
  "/:postId",
  [tokenMiddleware.validate as any, requestSchemaValidate(updatePostSchema)],
  postController.updatePost
);

router.post(
  "/likes",
  [tokenMiddleware.validate as any, requestSchemaValidate(addLikesSchema)],
  postController.addLikes
);

router.post(
  "/comments",
  [tokenMiddleware.validate as any, requestSchemaValidate(addCommentSchema)],
  postController.addComment
);

export default router;
