import bcrypt from 'bcrypt';
import { IUserModel, TInappAuth } from '../database/models/user';
import JsonWebToken from './token-service';
import UserRepository, { TUpdateUser } from '../repositories/user-repository';
import UserMapper from '../lib/mappers/user-mapper';
import {
    BadRequestError,
    NotFoundError,
} from '../lib/custom-errors/class-errors';

class UserService {
    private _repository = new UserRepository();

    public async getUserSsoEmail(email: string) {
        try {
            const user = await this._repository.findUserByEmail(email);

            if (!user) {
                throw new NotFoundError('User does not exists.');
            }
            return user;
        } catch (error) {
            throw error;
        }
    }

    public async getUsers() {
        try {
            return await this._repository.list();
        } catch (error) {
            throw error;
        }
    }

    public async updateUser(payload: TUpdateUser) {
        try {
            const updatedUser = await this._repository.update(payload);
            return updatedUser;
        } catch (error) {
            throw error;
        }
    }
}

class UserAuthService {
    private _repo = new UserRepository();
    private _jwt = new JsonWebToken();
    private _mapper = new UserMapper();

    public async signupInapp(payload: TInappAuth) {
        const isExists = await this._repo.checkInappUser(payload.contactNumber);
        if (isExists) throw new NotFoundError('User already exists.');
        payload.password = await bcrypt.hashSync(payload.password, 10);
        const newUser = await this._repo.create(
            this._mapper.createUser(payload) as IUserModel
        );

        const token = await this._jwt.sign({
            id: newUser.id,
            source: newUser.source,
        });

        return {
            token,
            user: {
                contactNumber: payload.contactNumber,
                source: payload.source,
            },
        };
    }

    public async login(payload: TInappAuth) {
        const user = await this._repo.checkInappUser(payload.contactNumber);
        if (!user || user.source === 'SSO') {
            throw new BadRequestError('Wrong username or password.');
        }

        const isMatched = await bcrypt.compareSync(
            payload.password,
            user?.password as string
        );
        if (!isMatched) {
            throw new BadRequestError('Wrong username or password.');
        }

        const token = await this._jwt.sign({
            id: user.id,
            source: user.source,
        });
        return {
            token,
            user: {
                contactNumber: payload.contactNumber,
                source: payload.source,
                hasProfile: user?.firstname ? true : false,
            },
        };
    }

    public async authSso(payload: IUserModel): Promise<Record<string, any>> {
        try {
            const hasAccount = await this._repo.findUserByEmail(
                payload.emailAddress
            );
            if (!hasAccount) {
                const mappedPayload = this._mapper.createUser(payload);
                const newUser = await this._repo.create(
                    mappedPayload as IUserModel
                );
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

            const token = await this._jwt.sign({
                id: hasAccount.id,
                source: payload.source,
            });
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
}

export { UserService, UserAuthService };
