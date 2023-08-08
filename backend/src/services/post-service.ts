import { IComments, IPost } from "../database/models/post";
import { NotFoundError } from "../lib/custom-errors/class-errors";
import PostLikesRepository from "../repositories/post-repository";

const dbInstance = new PostLikesRepository();

type TAddLikes = {
  userId: string;
  postId: string;
};

type TAddComment = {
  userId: string;
  postId?: string;
  comment: string;
  createdAt: string;
};

class Likes {
  public async addLikes(payload: TAddLikes) {
    const post = await dbInstance.findPostById(payload?.postId as string);
    if (!post) throw new NotFoundError();

    if (!post.likes?.includes(payload.userId)) {
      post.likes?.push(payload.userId);
    }

    const updatedPost = await dbInstance.update({ ...post });
    return updatedPost;
  }
}

class Comments {
  public async addComment(payload: TAddComment) {
    const post = await dbInstance.findPostById(payload?.postId as string);
    if (!post) throw new NotFoundError();

    delete payload.postId;
    post.comments?.push(payload as IComments);
    const updatedPost = await dbInstance.update({ ...post });
    return updatedPost;
  }
}

class PostService {
  private _likesInstance = new Likes();
  private _commentsInstance = new Comments();

  public async createPost(payload: IPost) {
    return await dbInstance.create(payload);
  }

  public async updatePost(payload: Partial<IPost>) {
    const isExist = await dbInstance.findPostById(payload?.id as string);
    if (!isExist) throw new NotFoundError("Post does not exist.");
    return await dbInstance.update(payload as IPost);
  }

  public async getPosts() {
    return await dbInstance.getPostsList();
  }

  public async addLikes(payload: TAddLikes) {
    return await this._likesInstance.addLikes(payload);
  }

  public async addComment(payload: TAddComment) {
    return await this._commentsInstance.addComment(payload);
  }
}

export default PostService;
