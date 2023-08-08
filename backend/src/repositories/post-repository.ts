import Firstore from "../database/firestore";
import { IPost } from "../database/models/post";

const instance = Firstore.getInstance();

class PostRepository {
  private _colName = "posts";
  private _db = instance.getDb();

  protected async createPost(payload: IPost) {
    try {
      const newData = await instance
        .setCollectionName(this._colName)
        .create<IPost>(payload);
      return newData;
    } catch (error) {
      throw error;
    }
  }

  // protected async updatePost(payload: IPost) {
  //   try {
  //     const newData = await this._db
  //       .collection(this._colName)
  //       .where("uid", "==", payload.uid);

  //     return (await newData.get()).docs[0].ref.update(payload);
  //   } catch (error) {
  //     throw error;
  //   }
  // }

  // private async findRef(uid: string) {
  //   try {
  //     const newData = await this._db
  //       .collection(this._colName)
  //       .where("uid", "==", uid)
  //       .get();

  //     return newData.docs.length > 0 ? newData. : null;
  //   } catch (error) {
  //     throw error;
  //   }
  // }
}

export default PostRepository;
