import type { Request, Response, NextFunction } from "express";
import type { Schema, ValidationError } from "joi";
import httpStatus from "http-status";
import ResponseCodes from "../commons/response-codes";

type TJoiError = {
  status: number;
  statusCode: string;
};

const requestSchemaValidate =
  (schema: Schema) => (req: Request, res: Response, next: NextFunction) => {
    const event = Object.keys(req.body).length === 0 ? req.query : req.body;
    const { error, value } = schema.validate(event, { abortEarly: false });

    if (error) {
      const joiError = error as Partial<ValidationError> & TJoiError;

      joiError.message = error.details![0]?.message || "joi error";
      joiError.status = httpStatus.BAD_REQUEST;
      joiError.statusCode = ResponseCodes.BAD_REQUEST;
      joiError.stack = error.stack;

      if (error?.details) {
        delete joiError?.details;
      }

      next(error);
    }

    if (Object.keys(req.body).length > 0) req.body = value;
    if (Object.keys(req.query).length > 0) req.query = value;

    next();
  };

export default requestSchemaValidate;
