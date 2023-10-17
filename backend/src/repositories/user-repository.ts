import Firstore from '../database/firestore';
import { AuthSource, IUserModel } from '../database/models/user';

export type TUpdateUser = Omit<Partial<IUserModel>, 'createdAt'>;
class UserRepository {
    private _colName = 'users';
    private _firestore = Firstore.getInstance();

    public async create(payload: IUserModel): Promise<IUserModel> {
        try {
            const newUser = await this._firestore
                .setCollectionName(this._colName)
                .setDocId(payload.id)
                .create(payload);
            return newUser as IUserModel;
        } catch (error) {
            throw error;
        }
    }

    public async getUserById(id: string): Promise<IUserModel> {
        const user = await this._firestore
            .getDb()
            .collection('users')
            .where('id', '==', id)
            .get();
        return (user.docs[0]?.data() as IUserModel) || null;
    }

    public async findUserByEmail(email: string, source: AuthSource) {
        try {
            const user = await this._firestore
                .getDb()
                .collection('users')
                .where('emailAddress', '==', email)
                .where('source', '==', source)
                .get();

            return (user.docs[0]?.data() as IUserModel) || null;
        } catch (error) {
            throw error;
        }
    }

    public async getUserByContact(contact: string, source: AuthSource) {
        try {
            const user = await this._firestore
                .getDb()
                .collection('users')
                .where('contactNumber', '==', contact)
                .where('source', '==', source)
                .get();

            return (user.docs[0]?.data() as IUserModel) || null;
        } catch (error) {
            throw error;
        }
    }

    public async list() {
        try {
            const users = await this._firestore
                .getDb()
                .collection(this._colName)
                .where('isAdmin', '==', false)
                .get();

            return users.docs.length === 0
                ? []
                : (users.docs.map((user) => user.data()) as IUserModel[]);
        } catch (error) {
            throw error;
        }
    }

    public async update(payload: TUpdateUser) {
        try {
            const user = await this._firestore
                .setCollectionName(this._colName)
                .setDocId(payload.id as string)
                .update(payload);

            return user ? (user as IUserModel) : null;
        } catch (error) {
            throw error;
        }
    }
}

export default UserRepository;
