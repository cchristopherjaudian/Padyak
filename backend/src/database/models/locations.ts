import { IBaseModel } from './model';

export interface ILocation extends IBaseModel {
  type: string;
  name: string;
  latitude: number;
  longitude: number;
  photoUrl: string;
  contact: string;
  ratings: string;
}
