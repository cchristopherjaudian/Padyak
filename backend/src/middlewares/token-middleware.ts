import { NextFunction, Request, Response } from "express";
import TokenService from "../services/token-service";
import { AuthenticationError } from "../lib/custom-errors/class-errors";
import { IUserModel } from "../database/models/user";
import Firestore from "../database/firestore";

export interface IRequestWithUser extends Request {
  user: IUserModel;
}

class TokenMiddleware {
  private _firestore = Firestore.getInstance();
  private _jwt = new TokenService();

  public validate = async (
    req: IRequestWithUser,
    res: Response,
    next: NextFunction
  ) => {
    try {
      const token = req.headers["authorization"];
      if (!token) {
        throw new AuthenticationError();
      }
      const verifiedToken = await this._jwt.verify(token as string);
      const user = await this._firestore
        .setCollectionName("users")
        .findById(verifiedToken.id);

      if (!user) throw new AuthenticationError();

      req.user = user as IUserModel;

      next();
    } catch (error) {
      next(error);
    }
  };
}

export default TokenMiddleware;
