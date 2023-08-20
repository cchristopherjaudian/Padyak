import { IAlertStatuses } from "./alert";
import { IBaseModel } from "./model";

export interface IUserAlerts extends IBaseModel {
  to: string[];
  level: number;
  location: string;
  uid: string;
  longitude: number;
  latitude: number;
  status: IAlertStatuses;
}
