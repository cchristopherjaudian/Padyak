import { IBaseModel } from "./model";
import { IUserModel } from "./user";

export interface IRegisteredUser extends Pick<IUserModel, "photoUrl"> {
  displayName: string;
  uid: string;
  paymentUrl: string;
}

export interface IEvent extends IBaseModel {
  month: string;
  year: string;
  eventDate: string;
  uid: string;
  name: string;
  photoUrl: string;
  registeredUser?: IRegisteredUser[];
}
