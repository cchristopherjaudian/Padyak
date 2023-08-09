import Firstore from "../database/firestore";
import { IUserModel } from "../database/models/user";

class UserRepository {
  private _colName = "users";
  private _firestore = Firstore.getInstance();

  public async create(payload: IUserModel): Promise<IUserModel> {
    try {
      const newUser = await this._firestore
        .setCollectionName(this._colName)
        .setDocId(payload.id)
        .create(payload);
      return newUser as IUserModel;
    } catch (error) {
      throw error;
    }
  }

  public async findUserByEmail(email: string) {
    try {
      const user = await this._firestore
        .getDb()
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
