import { v4 as uudiv4 } from "uuid";

type TCreateUpdateMapper = {
  post: string;
  distance: string;
  movingTime: string;
  uid: string;
  fromLocation: string;
  toLocation: string;
  caption: string;
  photoUrl: string;
  fromLong: number;
  toLong: number;
  fromLat: number;
  toLat: number;
};

class PostMapper {
  createPost(payload: TCreateUpdateMapper) {
    return {
      id: uudiv4(),
      createdAt: new Date().toISOString(),
      modifiedAt: "",
      ...payload,
    };
  }

  updatePost(payload: Partial<TCreateUpdateMapper>) {
    return {
      modifiedAt: new Date().toISOString(),
      ...payload,
    };
  }
}

export default PostMapper;
