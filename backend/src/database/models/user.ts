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
}

export type TInappAuth = {
    contactNumber: string;
    password: string;
    source: AuthSource;
};
