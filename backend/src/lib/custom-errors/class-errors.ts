import httpStatus from "http-status";
import ResponseCodes from "../../commons/response-codes";
import BaseError from "./base-error";

class NotFoundError extends BaseError {
  private status: number;
  private statusCode: string;

  constructor(message?: string) {
    super(message || "Not Found");

    this.statusCode = ResponseCodes.NOT_FOUND;
    this.status = httpStatus.NOT_FOUND;
  }
}

class BadRequestError extends BaseError {
  private status: number;
  private statusCode: string;

  constructor(message?: string) {
    super(message || "Bad Request");

    this.statusCode = ResponseCodes.BAD_REQUEST;
    this.status = httpStatus.BAD_REQUEST;
  }
}

class AuthenticationError extends BaseError {
  private status: number;
  private statusCode: string;

  constructor(message?: string) {
    super(message || "Unauthorize Request");

    this.statusCode = ResponseCodes.UNAUTHORIZE;
    this.status = httpStatus.UNAUTHORIZED;
  }
}

class ResourceConflictError extends BaseError {
  private status: number;
  private statusCode: string;

  constructor(message?: string) {
    super(message || "Conflict Resource");

    this.statusCode = ResponseCodes.RESOURCE_CONFLICT;
    this.status = httpStatus.CONFLICT;
  }
}

export {
  NotFoundError,
  BadRequestError,
  ResourceConflictError,
  AuthenticationError,
};
