import * as Joi from "joi";
import { v4 } from "uuid";

type IBaseRequest = {
  id?: any;
  createdAt?: any;
  modifiedAt?: any;
};

const baseRequestSchema: IBaseRequest = {
  id: Joi.string().optional().default(v4()),
  createdAt: Joi.string().optional().default(new Date().toISOString()),
  modifiedAt: Joi.string().optional().default(new Date().toISOString()),
};

export { baseRequestSchema };
