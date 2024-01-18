import { TEmergencyContact } from './contacts';
import { TRescueGroup } from './event';
import { IBaseModel } from './model';

export enum AuthSource {
  IN_APP = 'IN_APP',
  SSO = 'SSO',
}

export interface IUserModel extends IBaseModel {
  firstname: string;
  lastname: string;
  emailAddress: string;
  contactNumber: string;
  gender: string;
  birthday: string;
  height: string;
  weight: string;
  isAdmin: boolean;
  photoUrl: string;
  source: AuthSource;
  password?: string;
  emergencyContacts?: TEmergencyContact[];
}

export type TInappAuth = {
  contactNumber: string;
  password: string;
  source: AuthSource;
};

export type TCreateInappProfile = {
  id: string;
  firstname: string;
  lastname: string;
  emailAddress: string;
  gender: string;
  birthday: string;
  height: string;
  weight: string;
  isAdmin: boolean;
  photoUrl: string;
  source: AuthSource;
};

export type TUserProfile = TCreateInappProfile & {
  contactNumber: string;
  password: string;
};
