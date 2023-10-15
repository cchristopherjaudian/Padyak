import express from 'express';
import { userController } from '../controllers';
import requestSchemaValidate from '../middlewares/joi-middleware';
import {
    createUserSchema,
    getUserByEmailSchma,
    inappAuthSchema,
    updateUserSchema,
} from '../lib/joi-schemas/cyclist-schema';
import TokenMiddleware from '../middlewares/token-middleware';

const tokenMiddleware = new TokenMiddleware();
const router = express.Router();

router.post(
    '/sso/auth',
    [requestSchemaValidate(createUserSchema)],
    userController.authSso
);

router.post(
    '/inapp/signup',
    [requestSchemaValidate(inappAuthSchema)],
    userController.inappSignup
);

router.post(
    '/inapp/login',
    [requestSchemaValidate(inappAuthSchema)],
    userController.inappLogin
);

router.get(
    '/email',
    [requestSchemaValidate(getUserByEmailSchma)],
    userController.getUserByEmail
);

router.get(
    '/',
    [tokenMiddleware.endUserValidate as any],
    userController.getUserList
);

router.patch(
    '/',
    [
        tokenMiddleware.endUserValidate as any,
        requestSchemaValidate(updateUserSchema),
    ],
    userController.updateUser
);

export default router;
