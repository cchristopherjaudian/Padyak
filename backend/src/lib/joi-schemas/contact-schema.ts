import * as Joi from 'joi';

const createEmergencyContactSchema = Joi.object({
  firstname: Joi.string().trim().required(),
  lastname: Joi.string().trim().required(),
  contact: Joi.string().trim().min(11).max(11).required(),
});

const removeEmergencyContactSchema = Joi.object({
  contact: Joi.string().trim().min(11).max(11).required(),
});

export { createEmergencyContactSchema, removeEmergencyContactSchema };
