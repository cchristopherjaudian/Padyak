import Firstore from "../database/firestore";
import { TUsermodel } from "../database/models/user";

const instance = Firstore.getInstance();

class UserRepository {
  private _colName = "users";
  private _db = instance.getDb();

  protected async create(payload: TUsermodel) {
    try {
      const newData = await instance
        .setCollectionName(this._colName)
        .create(payload);
      return newData;
    } catch (error) {
      throw error;
    }
  }

  protected async findUserByEmail(email: string) {
    console.log("email", email);
    try {
      const user = await this._db
        .collection("users")
        .where("email", "==", email)
        .get();

      return user.docs[0]?.data() || null;
    } catch (error) {
      throw error;
    }
  }
}

export default UserRepository;
