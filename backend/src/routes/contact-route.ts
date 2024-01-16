import express from 'express';
import { contactController } from '../controllers';
import requestSchemaValidate from '../middlewares/joi-middleware';
import TokenMiddleware from '../middlewares/token-middleware';
import {
  createEmergencyContactSchema,
  removeEmergencyContactSchema,
} from '../lib/joi-schemas/contact-schema';

const router = express.Router();
const tokenMiddleware = new TokenMiddleware();

router
  .patch(
    '/',
    [
      tokenMiddleware.endUserValidate as any,
      requestSchemaValidate(createEmergencyContactSchema),
    ],
    contactController.createEmergencyContact
  )
  .patch(
    '/remove',
    [
      tokenMiddleware.endUserValidate as any,
      requestSchemaValidate(removeEmergencyContactSchema),
    ],
    contactController.removeEmergencyContact
  );

export default router;
