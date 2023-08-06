import type { Response, Request, NextFunction } from "express";
import httpStatus from "http-status";
import { UserService } from "../services/user-service";
import ResponseObject from "../lib/response-object";
import ResponseCodes from "../commons/response-codes";

const userInstance = new UserService();
const responseObject = new ResponseObject();

const createUser = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const newUser = await userInstance.createUser(req.body);

    responseObject.createResponse(
      res,
      httpStatus.OK,
      ResponseCodes.DATA_CREATED,
      newUser
    );
  } catch (error) {
    next(error);
  }
};

export default { createUser };
