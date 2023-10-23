import StorageMapper from '../lib/mappers/storage-mapper';
import StorageRepository, {
    TCreateStorage,
} from '../repositories/storage-repository';

class StorageService {
    private _repo = new StorageRepository();
    private _mapper = new StorageMapper();

    public async getStorage(id: string) {
        console.log('id', id);
        return this._repo.getStorage(id);
    }

    public async create(payload: Omit<TCreateStorage, 'id'>) {
        const currentStorage = await this._repo.getStorage('LATEST');
        const mapped = this._mapper.createStorage(payload);
        mapped.id = 'LATEST';

        if (!currentStorage) {
            return this._repo.create(mapped);
        }

        return this._repo.update(mapped);
    }
}

export default StorageService;
