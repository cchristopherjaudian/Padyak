import { v4 as uudiv4 } from 'uuid';
import { TLocationCreate } from '../../repositories/location-repository';
import DateUtils from '../date';

const date = DateUtils.getInstance();
class LocationMapper {
    public createLocation(locations: TLocationCreate[]) {
        return locations.map((location) => {
            const id = uudiv4();
            return {
                id,
                ...location,
                createdAt: date.getIsoDate(new Date()),
            };
        });
    }
}

export default LocationMapper;
