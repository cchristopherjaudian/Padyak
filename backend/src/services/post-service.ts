import { IComments, IPost } from "../database/models/post";
import { NotFoundError } from "../lib/custom-errors/class-errors";
import PostMapper from "../lib/mappers/post-mapper";
import PostLikesRepository, {
  TPostsQuery,
} from "../repositories/post-repository";

const dbInstance = new PostLikesRepository();

type TAddLikes = {
  postId?: string;
  uid: string;
  photoUrl: string;
  displayName: string;
  id: string;
};

type TAddComment = {
  id: string;
  uid: string;
  postId?: string;
  comment: string;
  createdAt: string;
  photoUrl: string;
  displayName: string;
};

class Likes {
  public async addLikes(payload: TAddLikes) {
    try {
      const post = await dbInstance.findPostById(payload?.postId as string);
      if (!post) throw new NotFoundError();

      const hasLiked = post.likes?.find((like) => like.uid === payload.uid);
      if (!hasLiked) {
        delete payload.postId;
        post.likes?.push(payload);
      } else {
        const removedLike = post.likes?.filter(
          (like) => like.uid !== payload.uid
        );
        post.likes = removedLike;
      }

      const updatedPost = await dbInstance.update({ ...post });
      return updatedPost;
    } catch (error) {
      throw error;
    }
  }
}

class Comments {
  public async addComment(payload: TAddComment) {
    try {
      const post = await dbInstance.findPostById(payload?.postId as string);
      if (!post) throw new NotFoundError();

      delete payload.postId;
      post.comments?.push(payload as IComments);
      await dbInstance.update({ ...post });
      return payload;
    } catch (error) {
      throw error;
    }
  }
}

class PostService {
  private _likesInstance = new Likes();
  private _commentsInstance = new Comments();
  private _mapper = new PostMapper();

  public async createPost(payload: IPost) {
    const mappedPost = this._mapper.createPost(payload);
    return await dbInstance.create(mappedPost);
  }

  public async updatePost(payload: Partial<IPost>) {
    console.log("update post ", payload);
    const isExist = await dbInstance.findPostById(payload?.id as string);
    if (!isExist) throw new NotFoundError("Post does not exist.");
    const mappedPayload = this._mapper.updatePost(payload);
    return await dbInstance.update(mappedPayload as IPost);
  }

  public async getPosts(query: TPostsQuery) {
    return await dbInstance.getPostsList(query);
  }

  public async addLikes(payload: TAddLikes) {
    const likes = this._mapper.addLikes(payload);
    return await this._likesInstance.addLikes(likes);
  }

  public async addComment(payload: TAddComment) {
    const comments = this._mapper.addComments(payload);
    return await this._commentsInstance.addComment(comments);
  }
}

export default PostService;
