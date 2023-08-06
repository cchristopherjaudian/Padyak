import { initializeApp } from "firebase-admin/app";
import * as firestoreDb from "firebase-admin/firestore";

export interface IFirestore {
  getDb: () => firestoreDb.Firestore;
}

export type TDocRef = firestoreDb.DocumentReference;
export type TDocSnapshot = firestoreDb.DocumentSnapshot;
export type TDocData = firestoreDb.DocumentData;

class Firestore implements IFirestore {
  private static _instance: Firestore;
  private _app;
  private _colName: string;

  private constructor() {
    this._app = initializeApp();
  }

  public static getInstance(): Firestore {
    Firestore._instance = Firestore._instance || new Firestore();
    return Firestore._instance;
  }

  public getDb() {
    return firestoreDb.getFirestore(this._app);
  }

  public setCollectionName(colName: string) {
    this._colName = colName;
    return this;
  }

  public async create(payload: { [key: string]: unknown }) {
    try {
      const newData = await this.getDb().collection(this._colName).add(payload);
      return (await newData.get()).data()!;
    } catch (error) {
      throw error;
    }
  }
}

export default Firestore;
