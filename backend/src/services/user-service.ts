import { TDocData, TDocSnapshot } from "../database/firestore";
import { TUsermodel } from "../database/models/user";
import UserRepository from "../repositories/user-repository";

class UserService extends UserRepository {
  public async createUser(
    payload: TUsermodel
  ): Promise<Record<string, boolean>> {
    try {
      const hasAccount = await this.findUserByEmail(payload.emailAddress);
      if (!hasAccount) {
        await this.create(payload);
        return { auth: true, newData: true };
      }

      return {
        auth: true,
        newData: false,
      };
    } catch (error) {
      throw error;
    }
  }
}

export { UserService };
