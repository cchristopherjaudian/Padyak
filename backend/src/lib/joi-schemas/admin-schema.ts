import * as Joi from 'joi';
import { AuthSource } from '../../database/models/user';

const genderDictionary = ['Male', 'Female', 'M', 'F'];

const createAdminSchema = Joi.object({
    firstname: Joi.string().required(),
    photoUrl: Joi.string().required(),
    lastname: Joi.string().required(),
    emailAddress: Joi.string().email().required(),
    contactNumber: Joi.string().min(11).max(11).required(),
    gender: Joi.string()
        .valid(...genderDictionary)
        .required(),
    birthday: Joi.string().required(),
    height: Joi.string().required(),
    weight: Joi.string().required(),
    isAdmin: Joi.boolean().optional().default(true),
    source: Joi.string()
        .trim()
        .valid(...Object.values(AuthSource))
        .required(),
});

export { createAdminSchema };
