import * as Joi from "joi";

const sendAlertSchema = Joi.object({
  to: Joi.array().items(Joi.string().min(11).max(11).required()).required(),
  level: Joi.number().required(),
  location: Joi.string().required(),
});

export { sendAlertSchema };
