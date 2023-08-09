import * as Joi from "joi";

const createPostSchema = Joi.object({
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
});

const addLikesSchema = Joi.object({
  postId: Joi.string().required(),
});

const addCommentSchema = Joi.object({
  postId: Joi.string().required(),
  comment: Joi.string().required(),
});

export { createPostSchema, updatePostSchema, addLikesSchema, addCommentSchema };
