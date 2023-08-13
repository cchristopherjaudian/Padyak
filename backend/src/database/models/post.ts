import { IBaseModel } from "./model";

export interface IComments
  extends Pick<IBaseModel, "modifiedAt" | "createdAt"> {
  comment: string;
  photoUrl: string;
  uid: string;
  displayName: string;
}

export interface ILikes {
  photoUrl: string;
  uid: string;
  displayName: string;
}

export interface IPost extends IBaseModel {
  likes?: ILikes[]; // array of users {UID}
  comments?: IComments[];
  uid: string;
  post: string;
  distance: string;
  movingTime: string;
  fromLocation: string;
  toLocation: string;
  caption: string;
  photoUrl: string;
  fromLong: number;
  toLong: number;
  fromLat: number;
  toLat: number;
}
