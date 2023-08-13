import type { Response, Request, NextFunction } from "express";
import httpStatus from "http-status";
import ResponseObject from "../lib/response-object";
import ResponseCodes from "../commons/response-codes";
import { IRequestWithUser } from "../middlewares/token-middleware";
import { AlertService, UserAlerts } from "../services/alert-serivce";
import Logger from "../commons/logger";

const responseObject = new ResponseObject();
const alertService = new AlertService();
const userAlertsService = new UserAlerts(alertService);
const logger = Logger.getInstance();

const sendAlert = async (req: Request, res: Response, next: NextFunction) => {
  logger.write.debug("Initializing {sendAlert} controller...");
  const request = req as IRequestWithUser;
  try {
    const alert = await userAlertsService.sendAlert({
      ...req.body,
      uid: request.user.id,
      displayName: `${request.user.firstname} ${request.user.firstname}`,
    });

    responseObject.createResponse(
      res,
      httpStatus.OK,
      ResponseCodes.DATA_CREATED,
      alert
    );
  } catch (error) {
    logger.write.error(error);
    next(error);
  }
};

export default { sendAlert };
