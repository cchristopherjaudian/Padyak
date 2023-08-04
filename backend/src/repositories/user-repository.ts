import { IFirestore } from "../database/firestore";

class UserRepository {
  private _db;

  constructor(db: IFirestore) {
    this._db = db.getDb();
  }

  public createUser() {}
}

export default UserRepository;
