import type { Response, Request } from 'express';
import httpStatus from 'http-status';
import ResponseObject from '../lib/response-object';
import ResponseCodes from '../commons/response-codes';
import LocationService from '../services/location-service';
import { TGetLocationQuery } from '../repositories/location-repository';
import { catchAsync } from '../lib/catch-async';

const responseObject = new ResponseObject();
const locationService = new LocationService();

const createLocations = catchAsync(async (req: Request, res: Response) => {
    const locations = await locationService.createLocation(req.body);
    responseObject.createResponse(
        res,
        httpStatus.OK,
        ResponseCodes.DATA_CREATED,
        locations
    );
});

const getLocations = catchAsync(async (req: Request, res: Response) => {
    const locations = await locationService.getLocations(
        req.query as TGetLocationQuery
    );
    responseObject.createResponse(
        res,
        httpStatus.OK,
        ResponseCodes.LIST_RETRIEVED,
        locations
    );
});

export default { createLocations, getLocations };
