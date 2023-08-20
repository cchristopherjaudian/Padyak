import * as Joi from "joi";

const sendAlertSchema = Joi.object({
  to: Joi.string().required(),
  level: Joi.number().required(),
  location: Joi.string().required(),
  longitude: Joi.any().required(),
  latitude: Joi.any().required(),
  status: Joi.string()
    .valid(...["ACTIVE", "COMPLETED"])
    .optional()
    .default("ACTIVE"),
});

const patchAlertSchema = Joi.object({
  status: Joi.string()
    .valid(...["ACTIVE", "COMPLETED"])
    .required(),
});

export { sendAlertSchema, patchAlertSchema };
