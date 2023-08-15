import { IBaseModel } from "./model";
import { IUserModel } from "./user";

export interface IComments
  extends Pick<IBaseModel, "modifiedAt" | "createdAt" | "id"> {
  comment: string;
  photoUrl: string;
  uid: string;
  displayName: string;
}

export interface ILikes {
  id: string;
  photoUrl: string;
  uid: string;
  displayName: string;
}

export interface IPost extends IBaseModel {
  likes?: ILikes[]; // array of users {UID}
  comments?: IComments[];
  author: Pick<IUserModel, "photoUrl" | "firstname" | "lastname" | "id">;
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
