import type { Response, Request, NextFunction } from "express";
import httpStatus from "http-status";
import { UserService } from "../services/user-service";
import ResponseObject from "../lib/response-object";
import ResponseCodes from "../commons/response-codes";
import { IRequestWithUser } from "../middlewares/token-middleware";

const userInstance = new UserService();
const responseObject = new ResponseObject();

const createUser = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const authenticated = await userInstance.createUser(req.body);

    responseObject.createResponse(
      res,
      httpStatus.OK,
      ResponseCodes.DATA_CREATED,
      authenticated
    );
  } catch (error) {
    next(error);
  }
};

const getUserByEmail = async (
  req: Request,
  res: Response,
  next: NextFunction
) => {
  try {
    const user = await userInstance.getUserByEmail(req.body.emailAddress);

    responseObject.createResponse(
      res,
      httpStatus.OK,
      ResponseCodes.DATA_RETRIEVED,
      user
    );
  } catch (error) {
    next(error);
  }
};

const updateUser = async (req: Request, res: Response, next: NextFunction) => {
  const request = req as IRequestWithUser;
  try {
    const user = await userInstance.updateUser({
      ...req.body,
      id: request.user.id,
    });

    responseObject.createResponse(
      res,
      httpStatus.OK,
      ResponseCodes.DATA_RETRIEVED,
      user!
    );
  } catch (error) {
    next(error);
  }
};

export default { createUser, getUserByEmail, updateUser };
