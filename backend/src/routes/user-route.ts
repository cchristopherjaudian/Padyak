import express from 'express';
import { userController } from '../controllers';
import requestSchemaValidate from '../middlewares/joi-middleware';
import {
    createUserSchema,
    getUserByEmailSchma,
    inappAuthSignupSchema,
    inappAuthLoginSchema,
    createInappProfileSchema,
    getInappUserProfile,
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
    [requestSchemaValidate(inappAuthSignupSchema)],
    userController.inappSignup
);

router.post(
    '/inapp/signin',
    [requestSchemaValidate(inappAuthLoginSchema)],
    userController.inappLogin
);

router.get(
    '/email',
    [requestSchemaValidate(getUserByEmailSchma)],
    userController.getUserByEmail
);

router.get(
    '/sso/auth',
    [
        requestSchemaValidate(getInappUserProfile),
        tokenMiddleware.endUserValidate as any,
    ],
    userController.getInappAuth
);

router.get(
    '/',
    [tokenMiddleware.endUserValidate as any],
    userController.getUserList
);

router.patch(
    '/inapp/profile',
    [
        tokenMiddleware.endUserValidate as any,
        requestSchemaValidate(createInappProfileSchema),
    ],
    userController.createInappProfile
);

export default router;
