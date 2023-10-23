import Firstore from '../database/firestore';
import { IStorage } from '../database/models/storage-model';

export type TCreateStorage = Omit<IStorage, 'createdAt'>;

class StorageRepository {
    private _colName = 'storage';
    private _firestore = Firstore.getInstance();

    public async getStorage(id: string) {
        try {
            const storage = await this._firestore
                .getDb()
                .collection(this._colName)
                .where('id', '==', id)
                .get();

            return storage.docs.length > 0
                ? (storage.docs.pop()?.data() as IStorage)
                : null;
        } catch (error) {
            throw error;
        }
    }

    public async create(payload: TCreateStorage) {
        try {
            const newStorage = await this._firestore
                .setCollectionName(this._colName)
                .setDocId(payload.id)
                .create(payload);

            return newStorage as IStorage;
        } catch (error) {
            throw error;
        }
    }

    public async update(payload: Partial<TCreateStorage>) {
        try {
            const storage = await this._firestore
                .setCollectionName(this._colName)
                .setDocId(payload.id as string)
                .update(payload);

            return storage ? (storage as IStorage) : null;
        } catch (error) {
            throw error;
        }
    }
}

export default StorageRepository;
