import { TDocData, TDocSnapshot } from "../database/firestore";
import { TUsermodel } from "../database/models/user";
import JsonWebToken from "../lib/jwt";
import UserRepository from "../repositories/user-repository";

class UserService extends UserRepository {
  private _jwt = new JsonWebToken();

  public async createUser(payload: TUsermodel): Promise<Record<string, any>> {
    try {
      const hasAccount = await this.findUserByEmail(payload.emailAddress);
      if (!hasAccount) {
        const newUser = await this.create(payload);
        const token = await this._jwt.sign({ id: newUser.id });
        return { auth: true, newData: true, token };
      }

      const token = await this._jwt.sign({ id: hasAccount.id });
      return {
        auth: true,
        newData: false,
        token,
      };
    } catch (error) {
      throw error;
    }
  }
}

export { UserService };
