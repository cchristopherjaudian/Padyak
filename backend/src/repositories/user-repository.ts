import Firstore from "../database/firestore";
import { TUsermodel } from "../database/models/user";

const instance = Firstore.getInstance();

class UserRepository {
  private _colName = "users";
  private _db = instance.getDb();

  protected async create(payload: TUsermodel) {
    const newData = await this._db.collection(this._colName).add(payload);
    return (await newData.get()).data()!;
  }
}

export default UserRepository;
