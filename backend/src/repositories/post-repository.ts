import type * as firestoreDb from "firebase-admin/firestore";

import Firstore from "../database/firestore";
import { IPost } from "../database/models/post";

export type TPostsQuery = {
  id?: string;
  uid?: string;
  limit?: number;
};

class PostLikesRepository {
  private _colName = "posts";
  private _firestore = Firstore.getInstance();

  public async create(payload: IPost) {
    try {
      const newData = await this._firestore
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
      const updatedData = await this._firestore
        .setCollectionName(this._colName)
        .setDocId(payload.id)
        .update(payload);

      return updatedData as IPost;
    } catch (error) {
      throw error;
    }
  }

  public async findPostById(id: string) {
    return (await this._firestore
      .setCollectionName(this._colName)
      .findById(id)) as IPost;
  }

  public async getPostsList(query: TPostsQuery) {
    let postsRef = await this._firestore.getDb().collection(this._colName);

    if (query?.id) {
      postsRef = postsRef.where(
        "id",
        "==",
        query.id
      ) as firestoreDb.CollectionReference;
    }

    if (query?.uid) {
      postsRef = postsRef.where(
        "author.id",
        "==",
        query.uid
      ) as firestoreDb.CollectionReference;
    }

    const mappedRef = (
      await postsRef
        .orderBy("createdAt", "desc")
        .limit(query.limit as number)
        .get()
    ).docs.map((k) => k.data());
    return mappedRef as IPost[];
  }
}

export default PostLikesRepository;
