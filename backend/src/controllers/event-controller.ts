import type { Response, Request, NextFunction } from "express";
import httpStatus from "http-status";
import ResponseObject from "../lib/response-object";
import ResponseCodes from "../commons/response-codes";
import EventService from "../services/event-service";
import { IRequestWithUser } from "../middlewares/token-middleware";

const eventInstance = new EventService();
const responseObject = new ResponseObject();

const createEvent = async (req: Request, res: Response, next: NextFunction) => {
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
    next(error);
  }
};

const getYearlyEvents = async (
  req: Request,
  res: Response,
  next: NextFunction
) => {
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
    next(error);
  }
};

export default { createEvent, getYearlyEvents };
