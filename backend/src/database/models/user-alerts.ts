import { IAlertStatuses } from './alert';
import { IBaseModel } from './model';
import { IUserModel } from './user';

export interface IUserAlerts extends IBaseModel {
    to: string[];
    level: number;
    location: string;
    uid: string;
    longitude: number;
    latitude: number;
    status: IAlertStatuses;
    sender: Pick<IUserModel, 'id' | 'firstname' | 'lastname' | 'photoUrl'>;
}
