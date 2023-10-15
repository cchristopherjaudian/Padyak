import type { Response, Request, NextFunction } from 'express';
import httpStatus from 'http-status';
import { UserAuthService, UserService } from '../services/user-service';
import ResponseObject from '../lib/response-object';
import ResponseCodes from '../commons/response-codes';
import { IRequestWithUser } from '../middlewares/token-middleware';

const userInstance = new UserService();
const userAuthInstance = new UserAuthService();
const responseObject = new ResponseObject();

const authSso = async (req: Request, res: Response, next: NextFunction) => {
    try {
        const authenticated = await userAuthInstance.authSso(req.body);
        responseObject.createResponse(
            res,
            httpStatus.OK,
            ResponseCodes.DATA_CREATED,
            authenticated
        );
    } catch (error) {
        next(error);
    }
};

const inappSignup = async (req: Request, res: Response, next: NextFunction) => {
    try {
        const authenticated = await userAuthInstance.signupInapp(req.body);
        responseObject.createResponse(
            res,
            httpStatus.OK,
            ResponseCodes.DATA_CREATED,
            authenticated
        );
    } catch (error) {
        next(error);
    }
};

const inappLogin = async (req: Request, res: Response, next: NextFunction) => {
    try {
        const authenticated = await userAuthInstance.login(req.body);
        responseObject.createResponse(
            res,
            httpStatus.OK,
            ResponseCodes.DATA_RETRIEVED,
            authenticated
        );
    } catch (error) {
        next(error);
    }
};

const getUserByEmail = async (
    req: Request,
    res: Response,
    next: NextFunction
) => {
    try {
        const user = await userInstance.getUserSsoEmail(
            req.query.emailAddress as string
        );

        responseObject.createResponse(
            res,
            httpStatus.OK,
            ResponseCodes.DATA_RETRIEVED,
            user
        );
    } catch (error) {
        next(error);
    }
};

const updateUser = async (req: Request, res: Response, next: NextFunction) => {
    const request = req as IRequestWithUser;
    try {
        const user = await userInstance.updateUser({
            ...req.body,
            id: request.user.id,
        });

        responseObject.createResponse(
            res,
            httpStatus.OK,
            ResponseCodes.DATA_RETRIEVED,
            user!
        );
    } catch (error) {
        next(error);
    }
};

const getUserList = async (req: Request, res: Response, next: NextFunction) => {
    try {
        const users = await userInstance.getUsers();

        responseObject.createResponse(
            res,
            httpStatus.OK,
            ResponseCodes.DATA_RETRIEVED,
            users
        );
    } catch (error) {
        next(error);
    }
};

export default {
    authSso,
    getUserByEmail,
    updateUser,
    getUserList,
    inappSignup,
    inappLogin,
};
