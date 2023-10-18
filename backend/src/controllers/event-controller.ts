import type { Response, Request, NextFunction } from 'express';
import httpStatus from 'http-status';
import ResponseObject from '../lib/response-object';
import ResponseCodes from '../commons/response-codes';
import { EventRegistration, EventService } from '../services/event-service';
import { IRequestWithUser } from '../middlewares/token-middleware';
import { TEventListQuery } from '../repositories/event-repository';
import { catchAsync } from '../lib/catch-async';

const eventInstance = new EventService();
const responseObject = new ResponseObject();
const eventRegister = new EventRegistration(eventInstance);

const createEvent = catchAsync(
    async (req: Request, res: Response, next: NextFunction) => {
        const request = req as IRequestWithUser;
        const { photoUrl, firstname, lastname, id } = request.user;
        const newEvent = await eventInstance.createEvent({
            ...req.body,
            author: {
                id,
                photoUrl,
                firstname,
                lastname,
            },
        });

        responseObject.createResponse(
            res,
            httpStatus.OK,
            ResponseCodes.DATA_CREATED,
            newEvent
        );
    }
);

const getYearlyEvents = catchAsync(
    async (req: Request, res: Response, next: NextFunction) => {
        const events = await eventInstance.getYearlyEvents(
            req.query.year as string
        );

        responseObject.createResponse(
            res,
            httpStatus.OK,
            ResponseCodes.LIST_RETRIEVED,
            events
        );
    }
);

const getEvents = catchAsync(
    async (req: Request, res: Response, next: NextFunction) => {
        const event = await eventInstance.getEvents(
            req.query as TEventListQuery
        );

        responseObject.createResponse(
            res,
            httpStatus.OK,
            ResponseCodes.DATA_RETRIEVED,
            event
        );
    }
);

const getEvent = catchAsync(
    async (req: Request, res: Response, next: NextFunction) => {
        const event = await eventInstance.getEvent(req.params.eventId);

        responseObject.createResponse(
            res,
            httpStatus.OK,
            ResponseCodes.DATA_RETRIEVED,
            event
        );
    }
);

const updateEvent = catchAsync(
    async (req: Request, res: Response, next: NextFunction) => {
        const request = req as IRequestWithUser;
        const { photoUrl, firstname, lastname } = request.user;
        const event = await eventInstance.update({
            id: req.params.eventId,
            ...req.body,
            author: {
                photoUrl,
                firstname,
                lastname,
            },
        });

        responseObject.createResponse(
            res,
            httpStatus.OK,
            ResponseCodes.DATA_MODIFIED,
            event
        );
    }
);

const registerEvent = catchAsync(
    async (req: Request, res: Response, next: NextFunction) => {
        const request = req as IRequestWithUser;
        const { photoUrl, firstname, lastname, id } = request.user;
        const event = await eventRegister.registerCyclist({
            eventId: req.params.eventId,
            ...req.body,
            user: {
                photoUrl,
                firstname,
                lastname,
                id,
            },
        });

        responseObject.createResponse(
            res,
            httpStatus.OK,
            ResponseCodes.DATA_MODIFIED,
            event
        );
    }
);

const getCurrentEvent = catchAsync(
    async (req: Request, res: Response, next: NextFunction) => {
        const event = await eventInstance.getCurrentEvent();

        responseObject.createResponse(
            res,
            httpStatus.OK,
            ResponseCodes.DATA_MODIFIED,
            event
        );
    }
);

const deleteEvents = catchAsync(
    async (req: Request, res: Response, next: NextFunction) => {
        const event = await eventInstance.deleteEvents(req.body.ids);

        responseObject.createResponse(
            res,
            httpStatus.OK,
            ResponseCodes.DATA_MODIFIED,
            event
        );
    }
);

export default {
    createEvent,
    getYearlyEvents,
    getEvent,
    getEvents,
    updateEvent,
    registerEvent,
    getCurrentEvent,
    deleteEvents,
};
