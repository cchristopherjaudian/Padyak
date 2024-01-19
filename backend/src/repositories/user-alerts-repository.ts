import type * as firestoreDb from 'firebase-admin/firestore';
import Firstore from '../database/firestore';
import { IAlertStatuses } from '../database/models/alert';
import { IUserAlerts } from '../database/models/user-alerts';
import { IUserModel } from '../database/models/user';

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
  sender: Pick<IUserModel, 'id' | 'firstname' | 'lastname' | 'photoUrl'>;
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
  sender: Pick<IUserModel, 'id' | 'firstname' | 'lastname' | 'photoUrl'>;
};

export type TUpdateAlertStatus = {
  id: string;
  status: IAlertStatuses;
};

export type TListUserAlerts = {
  status?: string;
};

class UserAlertsRepository {
  private _colName = 'user-alerts';
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

  public async list(query: TListUserAlerts) {
    try {
      let alertsRef = this._firestore.getDb().collection(this._colName);

      if (query?.status) {
        alertsRef = alertsRef.where(
          'status',
          '==',
          query.status
        ) as firestoreDb.CollectionReference;
      }

      const alerts = await alertsRef.orderBy('createdAt', 'desc').get();
      return alerts.docs.length > 0
        ? (alerts.docs.map((alert) => alert.data()) as IUserAlerts[])
        : [];
    } catch (error) {
      throw error;
    }
  }
}

export default UserAlertsRepository;
