import Firstore from "../database/firestore";
import { IAlertStatuses } from "../database/models/alert";
import { IUserAlerts } from "../database/models/user-alerts";

export type TUserSendAlert = {
  id: string;
  createdAt: string;
  to: string[];
  displayName: string;
  level: number;
  location: string;
  uid: string;
  longitude: number;
  latitude: number;
  status: IAlertStatuses;
};

export type TRawSendAlert = {
  id: string;
  createdAt: string;
  to: string;
  displayName: string;
  level: number;
  location: string;
  uid: string;
  longitude: number;
  latitude: number;
  status: IAlertStatuses;
};

export type TUpdateAlertStatus = {
  id: string;
  status: IAlertStatuses;
};

class UserAlertsRepository {
  private _colName = "user-alerts";
  private _firestore = Firstore.getInstance();

  public async create(payload: IUserAlerts): Promise<IUserAlerts> {
    try {
      const newUser = await this._firestore
        .setCollectionName(this._colName)
        .setDocId(payload.id as string)
        .create(payload);
      return newUser as IUserAlerts;
    } catch (error) {
      throw error;
    }
  }

  public async update(payload: TUpdateAlertStatus) {
    try {
      await this._firestore
        .getDb()
        .collection(this._colName)
        .doc(payload.id)
        .update(payload);

      return payload;
    } catch (error) {
      throw error;
    }
  }
}

export default UserAlertsRepository;
