import bcrypt from 'bcrypt';
import {
    AuthSource,
    IUserModel,
    TCreateInappProfile,
    TInappAuth,
} from '../database/models/user';
import JsonWebToken from './token-service';
import UserRepository, { TUpdateUser } from '../repositories/user-repository';
import UserMapper from '../lib/mappers/user-mapper';
import {
    AuthenticationError,
    BadRequestError,
    NotFoundError,
    ResourceConflictError,
} from '../lib/custom-errors/class-errors';

class UserService {
    private _repository = new UserRepository();

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

    public async createInappProfile(payload: TCreateInappProfile) {
        const hasEmail = await this._repository.findUserByEmail(
            payload.emailAddress,
            payload.source
        );
        if (hasEmail) throw new ResourceConflictError('Email already exists.');

        return this._repository.update(payload);
    }
}

class UserAuthService {
    private _repo = new UserRepository();
    private _jwt = new JsonWebToken();
    private _mapper = new UserMapper();

    public async getUserSsoEmail(email: string, source: AuthSource) {
        try {
            const user = await this._repo.findUserByEmail(email, source);

            if (!user) throw new NotFoundError('User does not exists.');

            const token = await this._jwt.sign({
                id: user.id,
                source: user.source,
            });

            return {
                user,
                token,
            };
        } catch (error) {
            throw error;
        }
    }

    public async getInappAuth(payload: Record<string, unknown>) {
        try {
            console.log('payload', payload);
            const user = await this._repo.getInappUser(
                payload.contact as string
            );

            if (!user) throw new NotFoundError('User does not exists.');

            const isMatched = await bcrypt.compareSync(
                payload.password as string,
                user.password as string
            );
            if (!isMatched) {
                throw new AuthenticationError('Wrong contact or password');
            }

            const token = await this._jwt.sign({
                id: user.id,
                source: user.source,
            });

            return { user, token };
        } catch (error) {
            throw error;
        }
    }

    public async signupInapp(payload: TInappAuth) {
        const isExists = await this._repo.getUserByContact(
            payload.contactNumber,
            payload.source
        );
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
        const user = await this._repo.getUserByContact(
            payload.contactNumber,
            payload.source
        );
        if (!user) {
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
            hasProfile: user?.firstname ? true : false,
            user: {
                contactNumber: payload.contactNumber,
                source: payload.source,
            },
        };
    }

    public async authSso(payload: IUserModel): Promise<Record<string, any>> {
        try {
            const hasAccount = await this._repo.findUserByEmail(
                payload.emailAddress,
                payload.source
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
