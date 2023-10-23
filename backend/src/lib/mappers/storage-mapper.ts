import { v4 as uudiv4 } from 'uuid';
import DateUtils from '../date';
import { TCreateStorage } from '../../repositories/storage-repository';

const date = DateUtils.getInstance();
class StorageMapper {
    public createStorage(payload: Omit<TCreateStorage, 'id'>) {
        const id = uudiv4();
        return {
            id,
            ...payload,
            createdAt: date.getIsoDate(new Date()),
        };
    }
}

export default StorageMapper;
