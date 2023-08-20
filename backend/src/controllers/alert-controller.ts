import type { Response, Request, NextFunction } from "express";
import httpStatus from "http-status";
import ResponseObject from "../lib/response-object";
import ResponseCodes from "../commons/response-codes";
import { IRequestWithUser } from "../middlewares/token-middleware";
import { AlertService, UserAlerts } from "../services/alert-serivce";

const responseObject = new ResponseObject();
const alertService = new AlertService();
const userAlertsService = new UserAlerts(alertService);

const sendAlert = async (req: Request, res: Response, next: NextFunction) => {
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
    next(error);
  }
};

const updateAlertStatus = async (
  req: Request,
  res: Response,
  next: NextFunction
) => {
  try {
    const alert = await userAlertsService.updateStatus({
      ...req.body,
      id: req.params.alertId,
    });

    responseObject.createResponse(
      res,
      httpStatus.OK,
      ResponseCodes.DATA_MODIFIED,
      alert
    );
  } catch (error) {
    next(error);
  }
};

const getUserAlerts = async (
  req: Request,
  res: Response,
  next: NextFunction
) => {
  try {
    const alerts = await userAlertsService.getUserAlerts(req.query);

    responseObject.createResponse(
      res,
      httpStatus.OK,
      ResponseCodes.LIST_RETRIEVED,
      alerts
    );
  } catch (error) {
    next(error);
  }
};

export default { sendAlert, updateAlertStatus, getUserAlerts };
