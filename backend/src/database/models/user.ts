import { IBaseModel } from "./model";

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
}
