import { initializeApp } from "firebase-admin/app";
import * as firestoreDb from "firebase-admin/firestore";

export interface IFirestore {
  getDb: () => firestoreDb.Firestore;
}

class Firestore implements IFirestore {
  private static _instance: Firestore;
  private _app;

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
}

export default Firestore;
