import type { Response, Request, NextFunction } from "express";
import httpStatus from "http-status";
import { UserService } from "../services/user-service";
import ResponseObject from "../lib/response-object";
import ResponseCodes from "../commons/response-codes";
import { IRequestWithUser } from "../middlewares/token-middleware";
import Logger from "../commons/logger";

const userInstance = new UserService();
const responseObject = new ResponseObject();
const logger = Logger.getInstance();

const createUser = async (req: Request, res: Response, next: NextFunction) => {
  logger.write.debug("Initializing {createUser} controller...");
  try {
    const authenticated = await userInstance.createUser(req.body);

    responseObject.createResponse(
      res,
      httpStatus.OK,
      ResponseCodes.DATA_CREATED,
      authenticated
    );
  } catch (error) {
    logger.write.error(error);
    next(error);
  }
};

const getUserByEmail = async (
  req: Request,
  res: Response,
  next: NextFunction
) => {
  logger.write.debug("Initializing {getUserByEmail} controller...");
  try {
    const user = await userInstance.getUserByEmail(req.body.emailAddress);

    responseObject.createResponse(
      res,
      httpStatus.OK,
      ResponseCodes.DATA_RETRIEVED,
      user
    );
  } catch (error) {
    logger.write.error(error);
    next(error);
  }
};

const updateUser = async (req: Request, res: Response, next: NextFunction) => {
  logger.write.debug("Initializing {updateUser} controller...");
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
    logger.write.error(error);
    next(error);
  }
};

export default { createUser, getUserByEmail, updateUser };
