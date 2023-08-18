import type * as firestoreDb from "firebase-admin/firestore";
import Firestore from "../database/firestore";
import { ILocation } from "../database/models/locations";

export type TLocationCreate = Omit<
  ILocation,
  "id" | "createdAt" | "modifiedAt"
>;

export type TGetLocationQuery = {
  type?: string;
};

class LocationRepository {
  private _colName = "locations";
  private _firestore = Firestore.getInstance();

  public async create(payload: ILocation[]) {
    try {
      const locations = await Promise.all(
        payload.map(async (location) => {
          return await this._firestore
            .setCollectionName(this._colName)
            .setDocId(location.id)
            .create(location);
        })
      );
      console.log("locations", locations);
      return locations as ILocation[];
    } catch (error) {
      throw error;
    }
  }

  public async getLocations(query: TGetLocationQuery) {
    try {
      let locationRef = await this._firestore.getDb().collection(this._colName);

      if (query?.type) {
        locationRef = locationRef.where(
          "type",
          "==",
          query.type
        ) as firestoreDb.CollectionReference;
      }

      const locations = await locationRef.get();
      return locations.docs.map((location) => location.data()) as ILocation[];
    } catch (error) {
      throw error;
    }
  }
}

export default LocationRepository;
