import { IBaseModel } from "./model";
import { IUserModel } from "./user";

export interface IRegisteredUser {
  paymentUrl: string;
  createdAt: string;
  user: Pick<IUserModel, "photoUrl" | "id" | "firstname" | "lastname">;
}

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
  author: Pick<IUserModel, "photoUrl" | "firstname" | "lastname" | "id">;
}
