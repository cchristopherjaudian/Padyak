import { IBaseModel } from './model';
import { IUserModel } from './user';

export enum EventPaymentStatus {
  PAID = 'PAID',
  UNPAID = 'UNPAID',
  REJECTED = 'REJECTED',
}

export enum EventPaymentTypes {
  OTC = 'OTC',
  GCASH = 'GCASH',
}

export interface IRegisteredUser {
  status: EventPaymentStatus;
  paymentUrl: string;
  eventId: string;
  paymentType: EventPaymentTypes;
  createdAt: string;
  user: Pick<IUserModel, 'id'>;
}

export type TRescueGroup = {
  contact: string;
  name: string;
  eventId?: string;
};

export interface IEvent extends IBaseModel {
  month: string;
  year: string;
  eventDate: string;
  name: string;
  photoUrl: string;
  startTime: string;
  endTime: string;
  eventDescription: string;
  award: string;
  registeredUser?: IRegisteredUser[];
  author: Pick<IUserModel, 'photoUrl' | 'firstname' | 'lastname' | 'id'>;
  rescueGroup: TRescueGroup[];
}

export type TPaymentStatusValidate = {
  eventId: string;
  userId: string;
};
