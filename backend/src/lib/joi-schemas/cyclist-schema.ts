import * as Joi from "joi";

const genderDictionary = ["Male", "Female", "M", "F"];

const createUserSchema = Joi.object({
  firstname: Joi.string().required(),
  photoUrl: Joi.string().required(),
  lastname: Joi.string().required(),
  emailAddress: Joi.string().email().required(),
  contactNumber: Joi.string().min(11).max(11).required(),
  gender: Joi.string()
    .valid(...genderDictionary)
    .required(),
  birthday: Joi.string().required(),
  height: Joi.string().required(),
  weight: Joi.string().required(),
  isAdmin: Joi.boolean().optional().default(false),
});

const getUserByEmailSchma = Joi.object({
  emailAddress: Joi.string().email().required(),
});

const updateUserSchema = Joi.object({
  firstname: Joi.string().optional(),
  photoUrl: Joi.string().optional(),
  lastname: Joi.string().optional(),
  emailAddress: Joi.string().email().optional(),
  contactNumber: Joi.string().min(11).max(11).optional(),
  gender: Joi.string()
    .valid(...genderDictionary)
    .optional(),
  birthday: Joi.string().optional(),
  height: Joi.string().optional(),
  weight: Joi.string().optional(),
});

export { createUserSchema, getUserByEmailSchma, updateUserSchema };
