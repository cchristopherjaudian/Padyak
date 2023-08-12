import { IUserModel } from "../database/models/user";
import JsonWebToken from "./token-service";
import UserRepository, { TUpdateUser } from "../repositories/user-repository";
import UserMapper from "../lib/mappers/user-mapper";
import { NotFoundError } from "../lib/custom-errors/class-errors";

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
        return {
          auth: true,
          newData: true,
          token,
          user: {
            firstname: newUser.firstname,
            photoUrl: newUser.photoUrl,
            lastname: newUser.lastname,
            isAdmin: newUser.isAdmin,
          },
        };
      }

      const token = await this._jwt.sign({ id: hasAccount.id });
      return {
        auth: true,
        newData: false,
        token,
        user: {
          firstname: hasAccount.firstname,
          photoUrl: hasAccount.photoUrl,
          lastname: hasAccount.lastname,
          isAdmin: hasAccount.isAdmin,
        },
      };
    } catch (error) {
      throw error;
    }
  }

  public async getUserByEmail(email: string) {
    try {
      const user = await this._firestore.findUserByEmail(email);

      if (!user) {
        throw new NotFoundError("User does not exists.");
      }
      return user;
    } catch (error) {
      throw error;
    }
  }

  public async updateUser(payload: TUpdateUser) {
    try {
      const updatedUser = await this._firestore.update(payload);
      return updatedUser;
    } catch (error) {
      throw error;
    }
  }
}

export { UserService };
