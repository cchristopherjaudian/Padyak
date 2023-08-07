import Firstore from "../database/firestore";
import { IUserModel } from "../database/models/user";

const instance = Firstore.getInstance();

class UserRepository {
  private _colName = "users";
  private _db = instance.getDb();

  protected async create(payload: IUserModel): Promise<IUserModel> {
    try {
      const newUser = await instance
        .setCollectionName(this._colName)
        .create<IUserModel>(payload);
      return newUser as IUserModel;
    } catch (error) {
      throw error;
    }
  }

  protected async findUserByEmail(email: string) {
    try {
      const user = await this._db
        .collection("users")
        .where("emailAddress", "==", email)
        .get();

      return user.docs[0]?.data() || null;
    } catch (error) {
      throw error;
    }
  }
}

export default UserRepository;
