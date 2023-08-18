import type { Response, Request, NextFunction } from "express";
import httpStatus from "http-status";
import ResponseObject from "../lib/response-object";
import ResponseCodes from "../commons/response-codes";
import LocationService from "../services/location-service";
import { TGetLocationQuery } from "../repositories/location-repository";

const responseObject = new ResponseObject();
const locationService = new LocationService();

const createLocations = async (
  req: Request,
  res: Response,
  next: NextFunction
) => {
  try {
    const locations = await locationService.createLocation(req.body);
    responseObject.createResponse(
      res,
      httpStatus.OK,
      ResponseCodes.DATA_CREATED,
      locations
    );
  } catch (error) {
    next(error);
  }
};

const getLocations = async (
  req: Request,
  res: Response,
  next: NextFunction
) => {
  try {
    const locations = await locationService.getLocations(
      req.query as TGetLocationQuery
    );
    responseObject.createResponse(
      res,
      httpStatus.OK,
      ResponseCodes.LIST_RETRIEVED,
      locations
    );
  } catch (error) {
    next(error);
  }
};

export default { createLocations, getLocations };
