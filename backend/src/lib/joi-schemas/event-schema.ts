import * as Joi from 'joi';
import DateUtils from '../date';
import {
  EventPaymentStatus,
  EventPaymentTypes,
} from '../../database/models/event';

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
  rescueGroup: Joi.array()
    .items(
      Joi.object().keys({
        contact: Joi.string().trim().required(),
      })
    )
    .min(1)
    .required(),
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
  paymentUrl: Joi.string().trim().required(),
  paymentType: Joi.string()
    .trim()
    .valid(...Object.values(EventPaymentTypes))
    .required(),
  modifiedAt: Joi.string().optional().default(date.getIsoDate(new Date())),
  createdAt: Joi.string().optional().default(date.getIsoDate(new Date())),
});

const deleteEventsSchema = Joi.object({
  ids: Joi.string().required(),
});

const updatePaymentStatusSchema = Joi.object({
  status: Joi.string()
    .trim()
    .valid(...Object.values(EventPaymentStatus))
    .required(),
  userId: Joi.string().trim().required(),
});

export {
  createEventSchema,
  getYearlyEventSchema,
  updateEventSchema,
  registerEventSchema,
  getEventsSchema,
  deleteEventsSchema,
  updatePaymentStatusSchema,
};
