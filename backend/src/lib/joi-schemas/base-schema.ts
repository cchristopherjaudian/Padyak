import * as Joi from "joi";
import { v4 } from "uuid";

type IBaseRequest = {
  uid?: any;
  createdAt?: any;
  modifiedAt?: any;
};

const baseRequestSchema: IBaseRequest = {
  uid: Joi.string().optional().default(v4()),
  createdAt: Joi.string().optional().default(new Date().toISOString()),
  modifiedAt: Joi.string().optional().default(new Date().toISOString()),
};

export { baseRequestSchema };
