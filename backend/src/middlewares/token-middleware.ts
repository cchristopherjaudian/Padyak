import { NextFunction, Request, Response } from "express";
import JsonWebToken from "../lib/jwt";
import { AuthenticationError } from "../lib/custom-errors/class-errors";
import { TUsermodel } from "../database/models/user";
import Firestore from "../database/firestore";

class TokenMiddleware {
  private _jwt = new JsonWebToken();
  private _firestore = Firestore.getInstance();

  public async validateToken(
    req: Request & { user: TUsermodel },
    res: Response,
    next: NextFunction
  ) {
    try {
      const token = req.headers["X-Auth-Token"];
      if (!token) {
        throw new AuthenticationError();
      }

      const verifiedToken = await this._jwt.verify(token as string);

      const user = await this._firestore
        .setCollectionName("users")
        .findById(verifiedToken.id);

      if (!user) throw new AuthenticationError();

      req.user = user as TUsermodel;

      next();
    } catch (error) {
      next(error);
    }
  }
}

export default TokenMiddleware;
