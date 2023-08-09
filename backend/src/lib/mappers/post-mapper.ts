import { v4 as uudiv4 } from "uuid";
type TCreateUpdateMapper = {
  post: string;
  distance: string;
  movingTime: string;
  location: string;
  uid: string;
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
