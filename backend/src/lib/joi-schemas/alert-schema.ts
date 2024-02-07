import * as Joi from 'joi';

const sendAlertSchema = Joi.object({
  to: Joi.string().required(),
  level: Joi.any().required(),
  location: Joi.string().required(),
  longitude: Joi.any().required(),
  latitude: Joi.any().required(),
  status: Joi.string()
    .valid(...['ACTIVE', 'COMPLETED'])
    .optional()
    .default('ACTIVE'),
  sender: Joi.string().trim().required(),
});

const sendPassThroughSchema = Joi.object({
  to: Joi.string().required(),
  message: Joi.string().trim().required(),
});

const patchAlertSchema = Joi.object({
  status: Joi.string()
    .valid(...['ACTIVE', 'COMPLETED'])
    .required(),
});

const getUserAlerts = Joi.object({
  status: Joi.string()
    .valid(...['ACTIVE', 'COMPLETED'])
    .optional(),
});

const sendNotificationSchema = Joi.object({
  message: Joi.string().trim().required(),
  topic: Joi.string().trim().required(),
});

export {
  sendAlertSchema,
  patchAlertSchema,
  getUserAlerts,
  sendNotificationSchema,
  sendPassThroughSchema,
};
