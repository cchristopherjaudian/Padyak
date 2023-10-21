import * as Joi from 'joi';
import { AuthSource } from '../../database/models/user';

const genderDictionary = ['Male', 'Female', 'M', 'F'];

const createUserSchema = Joi.object({
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
    isAdmin: Joi.boolean().optional().default(false),
    source: Joi.string()
        .trim()
        .valid(...Object.values(AuthSource))
        .required(),
});

const inappAuthSignupSchema = Joi.object({
    contactNumber: Joi.string().trim().min(11).max(11).required(),
    password: Joi.string()
        .pattern(/^[A-Z]/, 'Password should start with a capital letter')
        .pattern(
            /(?=.*\d)/,
            'Password should contain atleast contain 1 numeric value'
        )
        .pattern(
            /(?=.*[@#$!%*?&])/,
            'Password should atleast contain 1 special character'
        )
        .pattern(
            /[A-Za-z\d@$!%*?&]{7,}/,
            'Password should be 8 characters length'
        )
        .trim()
        .required(),
});

const inappAuthLoginSchema = Joi.object({
    contactNumber: Joi.string().trim().min(11).max(11).required(),
    password: Joi.string().trim().required(),
    source: Joi.string()
        .trim()
        .valid(...Object.values(AuthSource))
        .required(),
});

const getUserByEmailSchma = Joi.object({
    emailAddress: Joi.string().email().required(),
});

const updateUserSchema = Joi.object({
    firstname: Joi.string().optional(),
    photoUrl: Joi.string().optional(),
    lastname: Joi.string().optional(),
    emailAddress: Joi.string().email().optional(),
    contactNumber: Joi.string().min(11).max(11).optional(),
    gender: Joi.string()
        .valid(...genderDictionary)
        .optional(),
    birthday: Joi.string().optional(),
    height: Joi.string().optional(),
    weight: Joi.string().optional(),
});

const createInappProfileSchema = Joi.object({
    firstname: Joi.string().required(),
    photoUrl: Joi.string().required(),
    lastname: Joi.string().required(),
    emailAddress: Joi.string().email().required(),
    gender: Joi.string()
        .valid(...genderDictionary)
        .required(),
    birthday: Joi.string().required(),
    height: Joi.string().required(),
    weight: Joi.string().required(),
    source: Joi.string()
        .trim()
        .valid(...Object.values(AuthSource))
        .required(),
});

export {
    createUserSchema,
    getUserByEmailSchma,
    updateUserSchema,
    inappAuthSignupSchema,
    inappAuthLoginSchema,
    createInappProfileSchema,
};
