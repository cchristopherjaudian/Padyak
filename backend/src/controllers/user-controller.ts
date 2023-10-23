import type { Response, Request } from 'express';
import httpStatus from 'http-status';
import { UserAuthService, UserService } from '../services/user-service';
import ResponseObject from '../lib/response-object';
import ResponseCodes from '../commons/response-codes';
import { IRequestWithUser } from '../middlewares/token-middleware';
import { AuthSource } from '../database/models/user';
import { catchAsync } from '../lib/catch-async';

const userInstance = new UserService();
const userAuthInstance = new UserAuthService();
const responseObject = new ResponseObject();

const authSso = catchAsync(async (req: Request, res: Response) => {
    const authenticated = await userAuthInstance.authSso(req.body);
    responseObject.createResponse(
        res,
        httpStatus.OK,
        ResponseCodes.DATA_CREATED,
        authenticated
    );
});

const inappSignup = catchAsync(async (req: Request, res: Response) => {
    const authenticated = await userAuthInstance.signupInapp(req.body);
    responseObject.createResponse(
        res,
        httpStatus.OK,
        ResponseCodes.DATA_CREATED,
        authenticated
    );
});

const inappLogin = catchAsync(async (req: Request, res: Response) => {
    const authenticated = await userAuthInstance.login(req.body);
    responseObject.createResponse(
        res,
        httpStatus.OK,
        ResponseCodes.AUTHENTICATED,
        authenticated
    );
});

const createInappProfile = catchAsync(async (req: Request, res: Response) => {
    const request = req as IRequestWithUser;
    const profile = await userInstance.createInappProfile({
        ...req.body,
        id: request.user.id,
        source: request.user.source,
    });
    responseObject.createResponse(
        res,
        httpStatus.OK,
        ResponseCodes.DATA_MODIFIED,
        profile!
    );
});

const getUserByEmail = catchAsync(async (req: Request, res: Response) => {
    const user = await userAuthInstance.getUserSsoEmail(
        req.query.emailAddress as string,
        AuthSource.SSO
    );

    responseObject.createResponse(
        res,
        httpStatus.OK,
        ResponseCodes.DATA_RETRIEVED,
        user
    );
});

const getInappAuth = catchAsync(async (req: Request, res: Response) => {
    const user = await userAuthInstance.getInappAuth(req?.query);

    responseObject.createResponse(
        res,
        httpStatus.OK,
        ResponseCodes.DATA_RETRIEVED,
        user
    );
});

const updateUser = catchAsync(async (req: Request, res: Response) => {
    const request = req as IRequestWithUser;
    const user = await userInstance.updateUser({
        ...req.body,
        id: request.user.id,
    });

    responseObject.createResponse(
        res,
        httpStatus.OK,
        ResponseCodes.DATA_MODIFIED,
        user!
    );
});

const forgotPassword = catchAsync(async (req: Request, res: Response) => {
    const user = await userInstance.forgotPassword(req.body);

    responseObject.createResponse(
        res,
        httpStatus.OK,
        ResponseCodes.DATA_MODIFIED,
        user!
    );
});

const getUserList = catchAsync(async (req: Request, res: Response) => {
    const users = await userInstance.getUsers();

    responseObject.createResponse(
        res,
        httpStatus.OK,
        ResponseCodes.LIST_RETRIEVED,
        users
    );
});

export default {
    authSso,
    getUserByEmail,
    updateUser,
    getUserList,
    inappSignup,
    inappLogin,
    createInappProfile,
    getInappAuth,
    forgotPassword,
};
