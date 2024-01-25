import type { Response, Request } from 'express';
import httpStatus from 'http-status';
import ResponseObject from '../lib/response-object';
import ResponseCodes from '../commons/response-codes';
import { IRequestWithUser } from '../middlewares/token-middleware';
import { AlertService, UserAlerts } from '../services/alert-serivce';
import { VonageSMS } from '../services/sms-service';
import { catchAsync } from '../lib/catch-async';

const responseObject = new ResponseObject();
const alertService = new AlertService();
const userAlertsService = new UserAlerts(alertService);
const smsInstance = VonageSMS.getInstance();

const sendAlert = catchAsync(async (req: Request, res: Response) => {
  const request = req as IRequestWithUser;
  const { id, firstname, lastname, photoUrl } = request.user;
  const alert = await userAlertsService.sendAlert(smsInstance, {
    ...req.body,
    uid: request.user.id,
    displayName: `${request.user.firstname} ${request.user.firstname}`,
    sender: { id, firstname, lastname, photoUrl },
  });

  responseObject.createResponse(
    res,
    httpStatus.OK,
    ResponseCodes.DATA_CREATED,
    alert
  );
});

const updateAlertStatus = catchAsync(async (req: Request, res: Response) => {
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
});

const getUserAlerts = catchAsync(async (req: Request, res: Response) => {
  const alerts = await userAlertsService.getUserAlerts(req.query);

  responseObject.createResponse(
    res,
    httpStatus.OK,
    ResponseCodes.LIST_RETRIEVED,
    alerts
  );
});

const notifyAdmin = catchAsync(async (req: Request, res: Response) => {
  const alerts = await userAlertsService.notifyAdmin();

  responseObject.createResponse(
    res,
    httpStatus.OK,
    ResponseCodes.DATA_RETRIEVED,
    alerts
  );
});

export default { sendAlert, updateAlertStatus, getUserAlerts, notifyAdmin };
