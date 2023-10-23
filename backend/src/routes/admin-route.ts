import express from 'express';
import { storageController, userController } from '../controllers';
import requestSchemaValidate from '../middlewares/joi-middleware';
import {
    createAdminSchema,
    createAdminStorage,
} from '../lib/joi-schemas/admin-schema';
import TokenMiddleware from '../middlewares/token-middleware';

const router = express.Router();
const tokenMiddleware = new TokenMiddleware();

router.post(
    '/sso/auth',
    [requestSchemaValidate(createAdminSchema)],
    userController.authSso
);

router.post(
    '/storage',
    [
        tokenMiddleware.adminValidate as any,
        requestSchemaValidate(createAdminStorage),
    ],
    storageController.createStorage
);

router.get(
    '/storage/:storageId',
    [tokenMiddleware.endUserValidate as any],
    storageController.getStorage
);

export default router;
