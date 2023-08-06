import type { Request, Response, NextFunction } from "express";
import type { Schema, ValidationError } from "joi";
import httpStatus from "http-status";
import ResponseCodes from "../../commons/response-codes";
import { BadRequestError } from "../custom-errors/class-errors";

type TJoiError = {
  status: number;
  statusCode: string;
};

// const requestSchemaValidate =
//   (schema: Schema) =>
//   async (req: Request, res: Response, next: NextFunction) => {
//     try {
//       await schema.validateAsync(req.body, { abortEarly: false });

//       return next();
//     } catch (err) {
//       const error = err as Partial<ValidationError> & TJoiError;

//       error.message = error.details![0]?.message || "joi error";
//       error.status = httpStatus.BAD_REQUEST;
//       error.statusCode = ResponseCodes.BAD_REQUEST;

//       if (error?.details) {
//         delete error?.details;
//       }

//       console.log("error joi", error);
//       return next(error);
//     }
//   };
const requestSchemaValidate =
  (schema: Schema) => (req: Request, res: Response, next: NextFunction) => {
    const { error } = schema.validate(req.body, { abortEarly: false });

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

    next();
  };

export default requestSchemaValidate;
