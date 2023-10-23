import type { Response, Request } from 'express';
import httpStatus from 'http-status';
import ResponseObject from '../lib/response-object';
import ResponseCodes from '../commons/response-codes';
import { catchAsync } from '../lib/catch-async';
import StorageService from '../services/storage-service';

const responseObject = new ResponseObject();
const storage = new StorageService();

const createStorage = catchAsync(async (req: Request, res: Response) => {
    const newStorage = await storage.create(req.body);
    responseObject.createResponse(
        res,
        httpStatus.OK,
        ResponseCodes.DATA_CREATED,
        newStorage!
    );
});

const getStorage = catchAsync(async (req: Request, res: Response) => {
    const storageData = await storage.getStorage(req.params.storageId);
    responseObject.createResponse(
        res,
        httpStatus.OK,
        ResponseCodes.DATA_RETRIEVED,
        storageData!
    );
});

export default { createStorage, getStorage };
