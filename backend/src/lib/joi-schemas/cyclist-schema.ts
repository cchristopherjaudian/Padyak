import * as Joi from "joi";
import { baseRequestSchema } from "./base-schema";

const genderDictionary = ["Male", "Female", "M", "F"];

delete baseRequestSchema?.modifiedAt;

const createUserSchema = Joi.object({
  ...baseRequestSchema,
  firstname: Joi.string().required(),
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

export { createUserSchema };
