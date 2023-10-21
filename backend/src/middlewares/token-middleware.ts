import { NextFunction, Request, Response } from 'express';
import TokenService from '../services/token-service';
import { AuthenticationError } from '../lib/custom-errors/class-errors';
import { IUserModel } from '../database/models/user';
import Firestore from '../database/firestore';

export interface IRequestWithUser extends Request {
    user: IUserModel;
}

class TokenMiddleware {
    private _firestore = Firestore.getInstance();
    private _jwt = new TokenService();

    private async verifyToken(token: string): Promise<IUserModel> {
        try {
            if (!token) {
                throw new AuthenticationError();
            }
            const verifiedToken = await this._jwt.verify(token as string);
            const user = await this._firestore
                .setCollectionName('users')
                .findById(verifiedToken.id);

            if (!user) throw new AuthenticationError();

            return user as IUserModel;
        } catch (error) {
            throw error;
        }
    }

    public endUserValidate = async (
        req: IRequestWithUser,
        res: Response,
        next: NextFunction
    ) => {
        try {
            const token = req.headers['authorization'];
            const authUser = await this.verifyToken(token as string);

            req.user = authUser;

            next();
        } catch (error) {
            next(error);
        }
    };

    public adminValidate = async (
        req: IRequestWithUser,
        res: Response,
        next: NextFunction
    ) => {
        try {
            const token = req.headers['authorization'];
            const authAdmin = await this.verifyToken(token as string);

            if (!authAdmin.isAdmin) throw new AuthenticationError();

            req.user = authAdmin;

            next();
        } catch (error) {
            next(error);
        }
    };
}

export default TokenMiddleware;
