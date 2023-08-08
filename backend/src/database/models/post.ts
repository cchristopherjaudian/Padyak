import { IBaseModel } from "./model";

export interface IComments extends IBaseModel {
  comment: string;
}

export interface IPost extends IBaseModel {
  likes?: string[]; // array of users {UID}
  comments?: IComments[];
  post: string;
  distance: string;
  movingTime: string;
  location: string;
}
