import { IBaseModel } from "./model";

export interface IUserAlerts extends IBaseModel {
  to: string[];
  level: number;
  location: string;
  uid: string;
}
