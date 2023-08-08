import * as Joi from "joi";
import { v4 } from "uuid";
import { baseRequestSchema } from "./base-schema";

const { uid, createdAt, modifiedAt } = baseRequestSchema;

const createPostSchema = Joi.object({
  uid,
  createdAt,
  id: Joi.string().optional().default(v4()),
  likes: Joi.array().optional().default([]),
  comments: Joi.array().optional().default([]),
  post: Joi.string().required(),
  distance: Joi.string().required(),
  movingTime: Joi.string().required(),
  location: Joi.string().required(),
});

const updatePostSchema = Joi.object({
  post: Joi.string().optional(),
  distance: Joi.string().optional(),
  movingTime: Joi.string().optional(),
  location: Joi.string().optional(),
  modifiedAt: Joi.string().optional().default(new Date().toISOString()),
});

const addLikesSchema = Joi.object({
  postId: Joi.string().required(),
});

const addCommentSchema = Joi.object({
  postId: Joi.string().required(),
  comment: Joi.string().required(),
  createdAt,
});

export { createPostSchema, updatePostSchema, addLikesSchema, addCommentSchema };
