import express from 'express';
import { eventController } from '../controllers';
import requestSchemaValidate from '../middlewares/joi-middleware';
import TokenMiddleware from '../middlewares/token-middleware';
import {
    createEventSchema,
    deleteEventsSchema,
    getEventsSchema,
    getYearlyEventSchema,
    registerEventSchema,
    updateEventSchema,
    updatePaymentStatusSchema,
} from '../lib/joi-schemas/event-schema';

const router = express.Router();
const tokenMiddleware = new TokenMiddleware();

router.post(
    '/',
    [
        tokenMiddleware.adminValidate as any,
        requestSchemaValidate(createEventSchema),
    ],
    eventController.createEvent
);

router.get(
    '/count',
    [
        tokenMiddleware.adminValidate as any,
        requestSchemaValidate(getYearlyEventSchema),
    ],
    eventController.getYearlyEvents
);

router.get(
    '/',
    [
        tokenMiddleware.endUserValidate as any,
        requestSchemaValidate(getEventsSchema),
    ],
    eventController.getEvents
);

router.delete(
    '/',
    [
        tokenMiddleware.adminValidate as any,
        requestSchemaValidate(deleteEventsSchema),
    ],
    eventController.deleteEvents
);

router.get(
    '/now',
    [tokenMiddleware.endUserValidate as any],
    eventController.getCurrentEvent
);

router.get(
    '/:eventId',
    [tokenMiddleware.endUserValidate as any],
    eventController.getEvent
);

router.patch(
    '/:eventId',
    [
        tokenMiddleware.adminValidate as any,
        requestSchemaValidate(updateEventSchema),
    ],
    eventController.updateEvent
);

router.patch(
    '/cyclist/:eventId',
    [
        tokenMiddleware.endUserValidate as any,
        requestSchemaValidate(registerEventSchema),
    ],
    eventController.registerEvent
);

router.get(
    '/payment/:eventId',
    [tokenMiddleware.endUserValidate as any],
    eventController.validatePaymentStatus
);

router.patch(
    '/payment/:eventId',
    [
        tokenMiddleware.adminValidate as any,
        requestSchemaValidate(updatePaymentStatusSchema),
    ],
    eventController.updatePaymentStatus
);
export default router;
