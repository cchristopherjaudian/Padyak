import { IUserModel } from "../database/models/user";
import JsonWebToken from "./token-service";
import UserRepository from "../repositories/user-repository";
import UserMapper from "../lib/mappers/user-mapper";

class UserService {
  private _jwt = new JsonWebToken();
  private _mapper = new UserMapper();
  private _firestore = new UserRepository();

  public async createUser(payload: IUserModel): Promise<Record<string, any>> {
    try {
      const hasAccount = await this._firestore.findUserByEmail(
        payload.emailAddress
      );
      if (!hasAccount) {
        const mappedPayload = this._mapper.createUser(payload);
        const newUser = await this._firestore.create(mappedPayload);
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
