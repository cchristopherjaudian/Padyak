import * as Joi from "joi";

const sendAlertSchema = Joi.object({
  to: Joi.string().required(),
  level: Joi.number().required(),
  location: Joi.string().required(),
});

export { sendAlertSchema };
