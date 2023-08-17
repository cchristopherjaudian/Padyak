import * as Joi from "joi";
import DateUtils from "../date";

const date = DateUtils.getInstance();

const createEventSchema = Joi.object({
  month: Joi.string().required(),
  year: Joi.string().required(),
  eventDate: Joi.string().required(),
  name: Joi.string().required(),
  eventDescription: Joi.string().required(),
  photoUrl: Joi.string().required(),
  startTime: Joi.string().required(),
  endTime: Joi.string().required(),
  award: Joi.string().required(),
  registeredUser: Joi.array().optional().default([]),
});

const getYearlyEventSchema = Joi.object({
  year: Joi.string().required(),
});

const getEventsSchema = Joi.object({
  year: Joi.string().required(),
  month: Joi.string().required(),
});

const updateEventSchema = Joi.object({
  month: Joi.string().optional(),
  year: Joi.string().optional(),
  eventDate: Joi.string().optional(),
  name: Joi.string().optional(),
  photoUrl: Joi.string().optional(),
  modifiedAt: Joi.string().optional().default(date.getIsoDate(new Date())),
});

const registerEventSchema = Joi.object({
  photoUrl: Joi.string().required(),
  paymentUrl: Joi.string().required(),
  modifiedAt: Joi.string().optional().default(date.getIsoDate(new Date())),
});

export {
  createEventSchema,
  getYearlyEventSchema,
  updateEventSchema,
  registerEventSchema,
  getEventsSchema,
};
