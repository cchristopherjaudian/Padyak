import * as Joi from "joi";
import LOCATION_TYPES from "../../commons/location-types";

const getLocationsSchema = Joi.object({
  type: Joi.string()
    .valid(...Object.values(LOCATION_TYPES))
    .optional(),
});

const locationSchema = Joi.object({
  type: Joi.string()
    .valid(...Object.values(LOCATION_TYPES))
    .required(),
  name: Joi.string().required(),
  latitude: Joi.any().required(),
  longitude: Joi.any().required(),
  photoUrl: Joi.string().required(),
});

const createLocationsSchema = Joi.object({
  data: Joi.array().items(locationSchema).min(1).required(),
});

export { getLocationsSchema, createLocationsSchema };
