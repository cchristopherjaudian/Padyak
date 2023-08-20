import LocationMapper from "../lib/mappers/location-mapper";
import LocationRepository, {
  TGetLocationQuery,
  TLocationCreate,
} from "../repositories/location-repository";

class LocationService {
  private _repository = new LocationRepository();
  private _mapper = new LocationMapper();

  public async createLocation(payload: { data: TLocationCreate[] }) {
    try {
      const mappedLocations = this._mapper.createLocation(payload.data);
      return await this._repository.create(mappedLocations);
    } catch (error) {
      throw error;
    }
  }

  public async getLocations(query: TGetLocationQuery) {
    try {
      return await this._repository.getLocations(query);
    } catch (error) {
      throw error;
    }
  }
}

export default LocationService;
