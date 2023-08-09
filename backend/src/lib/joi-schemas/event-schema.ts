import * as Joi from "joi";

const createEventSchema = Joi.object({
  month: Joi.string().required(),
  year: Joi.string().required(),
  eventDate: Joi.string().required(),
  name: Joi.string().required(),
  photoUrl: Joi.string().required(),
  registeredUser: Joi.array().optional().default([]),
});

const getYearlyEventSchema = Joi.object({
  year: Joi.number().required(),
});

export { createEventSchema, getYearlyEventSchema };
