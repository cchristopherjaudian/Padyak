import { initializeApp } from "firebase-admin/app";
import * as firestoreDb from "firebase-admin/firestore";
import { v4 } from "uuid";
import Logger from "../commons/logger";

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
  private _logger = Logger.getInstance();

  private constructor() {
    this._app = initializeApp();
  }

  public static getInstance(): Firestore {
    Firestore._instance = Firestore._instance || new Firestore();
    return Firestore._instance;
  }

  public getDb() {
    this._logger.write.debug("connecting to firestore...");
    return firestoreDb.getFirestore(this._app);
  }

  public setCollectionName(colName: string) {
    this._logger.write.debug(
      `adding collection name with values ${colName}...`
    );
    this._colName = colName;
    return this;
  }

  public setDocId(id: string) {
    this._logger.write.debug(`adding document id with values ${id}...`);
    this._docId = id;
    return this;
  }

  public async create(payload: Record<string, any>) {
    this._logger.write.debug(
      `${this._colName} - creating firestore document...`
    );
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
    this._logger.write.debug(
      `${this._colName} - updating firestore document...`
    );
    try {
      await this.getDb()
        .collection(this._colName)
        .doc(this._docId)
        .update(payload);

      return payload;
    } catch (error) {
      throw error;
    }
  }

  public async findById(id: string) {
    this._logger.write.debug(
      `${this._colName} - finding firestore document by id with value ${id}...`
    );
    try {
      const ref = await this.getDb().collection(this._colName).doc(id).get();

      return ref.data() || null;
    } catch (error) {
      throw error;
    }
  }

  public async getAll<T>() {
    this._logger.write.debug(
      `${this._colName} - getting all firestore document...`
    );
    const refs = await this.getDb().collection(this._colName).get();
    const mappedRef = refs.docs.map((k) => k.data());
    return mappedRef as T[];
  }
}

export default Firestore;
