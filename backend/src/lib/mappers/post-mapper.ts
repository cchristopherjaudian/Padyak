import { v4 as uudiv4 } from "uuid";
import { IUserModel } from "../../database/models/user";
import DateUtils from "../date";

type TCreateUpdateMapper = {
  post: string;
  distance: string;
  author: Pick<IUserModel, "photoUrl" | "firstname" | "lastname" | "id">;
  movingTime: string;
  fromLocation: string;
  toLocation: string;
  caption: string;
  photoUrl: string;
  fromLong: number;
  toLong: number;
  fromLat: number;
  toLat: number;
};

type TAddLikes = {
  postId?: string;
  uid: string;
  photoUrl: string;
  displayName: string;
};

type TAddComment = {
  uid: string;
  postId?: string;
  comment: string;
  createdAt: string;
  photoUrl: string;
  displayName: string;
};

const date = DateUtils.getInstance();

class PostMapper {
  createPost(payload: TCreateUpdateMapper) {
    return {
      id: uudiv4(),
      createdAt: date.getIsoDate(new Date().toDateString()),
      modifiedAt: "",
      ...payload,
    };
  }

  updatePost(payload: Partial<TCreateUpdateMapper>) {
    return {
      modifiedAt: date.getIsoDate(new Date().toDateString()),
      ...payload,
    };
  }

  addLikes(payload: TAddLikes) {
    return {
      ...payload,
      id: uudiv4(),
    };
  }
  addComments(payload: TAddComment) {
    return {
      ...payload,
      id: uudiv4(),
    };
  }
}

export default PostMapper;
