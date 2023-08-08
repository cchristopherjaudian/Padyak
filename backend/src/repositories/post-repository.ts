import Firstore from "../database/firestore";
import { IPost } from "../database/models/post";

const instance = Firstore.getInstance();

class PostRepository {
  private _colName = "posts";
  private _db = instance.getDb();

  protected async create(payload: IPost) {
    try {
      const newData = await instance
        .setCollectionName(this._colName)
        .setDocId(payload.uid)
        .create(payload);
      return newData as IPost;
    } catch (error) {
      throw error;
    }
  }

  protected async update(payload: IPost) {
    try {
      const updatedData = await instance
        .setCollectionName(this._colName)
        .setDocId(payload.uid)
        .update(payload);

      return updatedData as IPost;
    } catch (error) {
      throw error;
    }
  }

  protected async findPostById(id: string) {
    return (await instance.findById(id)) as IPost;
  }

  protected async getPostsList() {
    return instance.getAll<IPost[]>();
  }
}

export default PostRepository;
