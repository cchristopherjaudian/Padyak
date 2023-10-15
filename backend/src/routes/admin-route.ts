import express from 'express';
import { userController } from '../controllers';
import requestSchemaValidate from '../middlewares/joi-middleware';
import { createAdminSchema } from '../lib/joi-schemas/admin-schema';

const router = express.Router();

router.post(
    '/sso/auth',
    [requestSchemaValidate(createAdminSchema)],
    userController.authSso
);

export default router;
