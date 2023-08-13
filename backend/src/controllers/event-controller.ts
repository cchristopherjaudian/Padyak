import type { Response, Request, NextFunction } from "express";
import httpStatus from "http-status";
import ResponseObject from "../lib/response-object";
import ResponseCodes from "../commons/response-codes";
import { EventRegistration, EventService } from "../services/event-service";
import { IRequestWithUser } from "../middlewares/token-middleware";
import Logger from "../commons/logger";

const eventInstance = new EventService();
const responseObject = new ResponseObject();
const eventRegister = new EventRegistration(eventInstance);
const logger = Logger.getInstance();

const createEvent = async (req: Request, res: Response, next: NextFunction) => {
  logger.write.debug("Initializing {createEvent} controller...");
  const request = req as IRequestWithUser;
  try {
    const newEvent = await eventInstance.createEvent({
      ...req.body,
      uid: request.user.id,
    });

    responseObject.createResponse(
      res,
      httpStatus.OK,
      ResponseCodes.DATA_CREATED,
      newEvent
    );
  } catch (error) {
    logger.write.error(error);
    next(error);
  }
};

const getYearlyEvents = async (
  req: Request,
  res: Response,
  next: NextFunction
) => {
  logger.write.debug("Initializing {getYearlyEvents} controller...");
  const request = req as IRequestWithUser;
  try {
    const events = await eventInstance.getYearlyEvents(
      req.query.year as string,
      request.user.id
    );

    responseObject.createResponse(
      res,
      httpStatus.OK,
      ResponseCodes.LIST_RETRIEVED,
      events
    );
  } catch (error) {
    logger.write.error(error);
    next(error);
  }
};

const getEvent = async (req: Request, res: Response, next: NextFunction) => {
  logger.write.debug("Initializing {getEvent} controller...");
  try {
    const event = await eventInstance.getEvent(req.params.eventId);

    responseObject.createResponse(
      res,
      httpStatus.OK,
      ResponseCodes.DATA_RETRIEVED,
      event
    );
  } catch (error) {
    logger.write.error(error);
    next(error);
  }
};

const updateEvent = async (req: Request, res: Response, next: NextFunction) => {
  logger.write.debug("Initializing {registerEvent} controller...");
  const request = req as IRequestWithUser;
  try {
    const event = await eventInstance.update({
      id: req.params.eventId,
      uid: request.user.id,
      displayName: `${request.user.firstname} ${request.user.lastname}`,
      ...req.body,
    });

    responseObject.createResponse(
      res,
      httpStatus.OK,
      ResponseCodes.DATA_MODIFIED,
      event
    );
  } catch (error) {
    logger.write.error(error);
    next(error);
  }
};

const registerEvent = async (
  req: Request,
  res: Response,
  next: NextFunction
) => {
  logger.write.debug("Initializing {registerEvent} controller...");
  try {
    const event = await eventRegister.registerCyclist({
      eventId: req.params.eventId,
      ...req.body,
    });

    responseObject.createResponse(
      res,
      httpStatus.OK,
      ResponseCodes.DATA_MODIFIED,
      event
    );
  } catch (error) {
    logger.write.error(error);
    next(error);
  }
};

export default {
  createEvent,
  getYearlyEvents,
  getEvent,
  updateEvent,
  registerEvent,
};
