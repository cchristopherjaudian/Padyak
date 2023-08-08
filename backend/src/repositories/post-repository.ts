import Firstore from "../database/firestore";
import { IPost } from "../database/models/post";

const instance = Firstore.getInstance();

class PostLikesRepository {
  private _colName = "posts";
  private _db = instance.getDb();

  public async create(payload: IPost) {
    try {
      const newData = await instance
        .setCollectionName(this._colName)
        .setDocId(payload.id)
        .create(payload);
      return newData as IPost;
    } catch (error) {
      throw error;
    }
  }

  public async update(payload: IPost) {
    try {
      const updatedData = await instance
        .setCollectionName(this._colName)
        .setDocId(payload.id)
        .update(payload);

      return updatedData as IPost;
    } catch (error) {
      throw error;
    }
  }

  public async findPostById(id: string) {
    return (await instance
      .setCollectionName(this._colName)
      .findById(id)) as IPost;
  }

  public async getPostsList() {
    return instance.setCollectionName(this._colName).getAll<IPost>();
  }
}

export default PostLikesRepository;
