import * as Joi from "joi";

const genderDictionary = ["Male", "Female", "M", "F"];

const createUserSchema = Joi.object({
  firstname: Joi.string().required(),
  lastname: Joi.string().required(),
  emailAddress: Joi.string().email().required(),
  contactNumber: Joi.string().min(11).required(),
  gender: Joi.string()
    .valid(...genderDictionary)
    .required(),
  birthday: Joi.string().required(),
  height: Joi.string().required(),
  weight: Joi.string().required(),
  isAdmin: Joi.boolean().required().default(false),
});

export { createUserSchema };
