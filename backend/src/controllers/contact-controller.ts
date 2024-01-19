import type { Response, Request } from 'express';
import httpStatus from 'http-status';
import ResponseObject from '../lib/response-object';
import ResponseCodes from '../commons/response-codes';
import { IRequestWithUser } from '../middlewares/token-middleware';
import { catchAsync } from '../lib/catch-async';
import { EmergencyContacts } from '../services/contacts-service';

const responseObject = new ResponseObject();
const contact = new EmergencyContacts();

const createEmergencyContact = catchAsync(
  async (req: Request, res: Response) => {
    const request = req as IRequestWithUser;
    const { id } = request.user;

    const newContact = await contact.createEmergencyContact({
      ...req.body,
      userId: id,
    });

    responseObject.createResponse(
      res,
      httpStatus.OK,
      ResponseCodes.DATA_CREATED,
      newContact!
    );
  }
);

const removeEmergencyContact = catchAsync(
  async (req: Request, res: Response) => {
    const request = req as IRequestWithUser;
    const { id } = request.user;

    const updatedContact = await contact.removeEmergencyContact({
      ...req.body,
      userId: id,
    });

    responseObject.createResponse(
      res,
      httpStatus.OK,
      ResponseCodes.DATA_DELETED,
      updatedContact!
    );
  }
);

const getEmergencyContacts = catchAsync(async (req: Request, res: Response) => {
  const request = req as IRequestWithUser;
  const { id } = request.user;

  const newContact = await contact.getEmergencyContacts({
    userId: id,
  });

  responseObject.createResponse(
    res,
    httpStatus.OK,
    ResponseCodes.LIST_RETRIEVED,
    newContact!
  );
});

export default {
  createEmergencyContact,
  removeEmergencyContact,
  getEmergencyContacts,
};
