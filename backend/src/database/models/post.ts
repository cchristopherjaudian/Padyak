import { IBaseModel } from "./model";

export interface IComments
  extends Pick<IBaseModel, "modifiedAt" | "createdAt"> {
  comment: string;
  userId: string;
}

export interface IPost extends IBaseModel {
  id: string;
  likes?: string[]; // array of users {UID}
  comments?: IComments[];
  post: string;
  distance: string;
  movingTime: string;
  location: string;
}
