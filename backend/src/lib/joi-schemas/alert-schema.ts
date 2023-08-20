import * as Joi from "joi";

const sendAlertSchema = Joi.object({
  to: Joi.string().required(),
  level: Joi.number().required(),
  location: Joi.string().required(),
  longitude: Joi.any().required(),
  latitude: Joi.any().required(),
});

export { sendAlertSchema };
