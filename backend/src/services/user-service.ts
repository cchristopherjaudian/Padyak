import { TDocData, TDocSnapshot } from "../database/firestore";
import { TUsermodel } from "../database/models/user";
import UserRepository from "../repositories/user-repository";

class UserService extends UserRepository {
  public async createUser(payload: TUsermodel): Promise<TDocData> {
    try {
      return await this.create(payload);
    } catch (error) {
      throw error;
    }
  }
}

class AdminService extends UserRepository {
  public async createUser(payload: TUsermodel): Promise<TUsermodel> {
    try {
      return await this.createUser(payload);
    } catch (error) {
      throw error;
    }
  }
}

export { UserService, AdminService };
