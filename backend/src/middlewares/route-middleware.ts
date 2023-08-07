import { NextFunction, Response, Request } from "express";
import httpStatus from "http-status";
import ResponseCodes from "../commons/response-codes";
import { NotFoundError } from "../lib/custom-errors/class-errors";

type TResponseError = {
  statusCode?: string;
  status?: number;
  message?: string;
  stack?: string;
};

export type TNormalizedError = Error & TResponseError;

class RouteMiddleware {
  public notFound = (req: Request, res: Response, next: NextFunction) => {
    const error = new NotFoundError(`cannot find -> ${req.originalUrl}`);
    res.status(httpStatus.NOT_FOUND);
    next(error);
  };

  public errorResponse = (
    error: TNormalizedError,
    req: Request,
    res: Response,
    next: NextFunction
  ) => {
    const status = error.status || httpStatus.INTERNAL_SERVER_ERROR;
    const statusCode = error.statusCode || ResponseCodes.INTERNAL_SERVER_ERROR;
    res.status(status).json({
      status,
      code: statusCode,
      data: {
        message: error.message,
        stack: process.env.NODE_ENV === "production" ? "" : error.stack,
      },
    });
  };
}

export default RouteMiddleware;
