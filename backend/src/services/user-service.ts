import { IUserModel } from "../database/models/user";
import JsonWebToken from "./token-service";
import UserRepository from "../repositories/user-repository";

class UserService extends UserRepository {
  private _jwt = new JsonWebToken();

  public async createUser(payload: IUserModel): Promise<Record<string, any>> {
    try {
      const hasAccount = (await this.findUserByEmail(
        payload.emailAddress
      )) as IUserModel;
      if (!hasAccount) {
        const newUser = (await this.create(payload)) as IUserModel;
        const token = await this._jwt.sign({ id: newUser.uid });
        return { auth: true, newData: true, token };
      }

      const token = await this._jwt.sign({ id: hasAccount.uid });
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
