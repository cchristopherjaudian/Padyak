import * as Joi from "joi";

const createPostSchema = Joi.object({
  likes: Joi.array().optional().default([]),
  comments: Joi.array().optional().default([]),
  post: Joi.string().required(),
  distance: Joi.string().required(),
  movingTime: Joi.string().required(),
  fromLocation: Joi.string().required(),
  toLocation: Joi.string().required(),
  caption: Joi.string().required(),
  photoUrl: Joi.string().required(),
  fromLong: Joi.number().required(),
  toLong: Joi.number().required(),
  fromLat: Joi.number().required(),
  toLat: Joi.number().required(),
});

const updatePostSchema = Joi.object({
  post: Joi.string().optional(),
  distance: Joi.string().optional(),
  movingTime: Joi.string().optional(),
  fromLocation: Joi.string().optional(),
  toLocation: Joi.string().optional(),
  caption: Joi.string().optional(),
  photoUrl: Joi.string().optional(),
  fromLong: Joi.number().optional(),
  toLong: Joi.number().optional(),
  fromLat: Joi.number().optional(),
  toLat: Joi.number().optional(),
});

const addLikesSchema = Joi.object({
  postId: Joi.string().required(),
  liked: Joi.number()
    .valid(...[1, 0])
    .required(),
});

const addCommentSchema = Joi.object({
  postId: Joi.string().required(),
  comment: Joi.string().required(),
});

export { createPostSchema, updatePostSchema, addLikesSchema, addCommentSchema };
