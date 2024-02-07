import express from 'express';
import { alertController } from '../controllers';
import requestSchemaValidate from '../middlewares/joi-middleware';
import TokenMiddleware from '../middlewares/token-middleware';
import {
  getUserAlerts,
  patchAlertSchema,
  sendAlertSchema,
  sendNotificationSchema,
} from '../lib/joi-schemas/alert-schema';

const router = express.Router();
const tokenMiddleware = new TokenMiddleware();

router.post(
  '/',
  [
    tokenMiddleware.endUserValidate as any,
    requestSchemaValidate(sendAlertSchema),
  ],
  alertController.sendAlert
);

router.post(
  '/passthrough',
  [
    tokenMiddleware.endUserValidate as any,
    requestSchemaValidate(sendAlertSchema),
  ],
  alertController.sendPassThrough
);

router.post(
  '/admin/notify',
  [
    tokenMiddleware.endUserValidate as any,
    requestSchemaValidate(sendAlertSchema),
  ],
  alertController.notifyAdmin
);

router.post(
  '/notify',
  [
    tokenMiddleware.endUserValidate as any,
    requestSchemaValidate(sendNotificationSchema),
  ],
  alertController.sendNotification
);

router.patch(
  '/:alertId',
  [
    tokenMiddleware.adminValidate as any,
    requestSchemaValidate(patchAlertSchema),
  ],
  alertController.updateAlertStatus
);

router.get(
  '/',
  [tokenMiddleware.adminValidate as any, requestSchemaValidate(getUserAlerts)],
  alertController.getUserAlerts
);

export default router;
