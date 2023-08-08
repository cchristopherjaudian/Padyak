import { initializeApp } from "firebase-admin/app";
import * as firestoreDb from "firebase-admin/firestore";
import { v4 } from "uuid";

export interface IFirestore {
  getDb: () => firestoreDb.Firestore;
}

export type TDocRef = firestoreDb.DocumentReference;
export type TDocSnapshot = firestoreDb.DocumentSnapshot;
export type TDocData = firestoreDb.DocumentData;
export type TDocumentData =
  FirebaseFirestore.DocumentSnapshot<FirebaseFirestore.DocumentData>;

class Firestore implements IFirestore {
  private static _instance: Firestore;
  private _app;
  private _colName: string;
  private _docId: string;

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

  public setDocId(id: string) {
    this._docId = id;
    return this;
  }

  public async create(payload: Record<string, any>) {
    try {
      await this.getDb()
        .collection(this._colName)
        .doc(this._docId)
        .set(payload);

      return payload;
    } catch (error) {
      throw error;
    }
  }

  public async update(payload: Record<string, any>) {
    try {
      await this.getDb()
        .collection(this._colName)
        .doc(this._docId)
        .update(payload);

      return payload;
    } catch (error) {}
  }

  public async findById(id: string) {
    try {
      const { docs } = await this.getDb()
        .collection(this._colName)
        .where("uid", "==", id)
        .get();

      return docs.length > 0 ? docs[0].data() : null;
    } catch (error) {
      throw error;
    }
  }

  public async getAll<T>() {
    const refs = await this.getDb().collection(this._colName).get();
    const mappedRef = refs.docs.map((k) => k.data());
    return mappedRef as T;
  }
}

export default Firestore;
