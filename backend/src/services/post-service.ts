import { IComments, IPost } from "../database/models/post";
import { NotFoundError } from "../lib/custom-errors/class-errors";
import PostMapper from "../lib/mappers/post-mapper";
import PostLikesRepository from "../repositories/post-repository";

const dbInstance = new PostLikesRepository();

type TAddLikes = {
  uid: string;
  postId?: string;
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

class Likes {
  public async addLikes(payload: TAddLikes) {
    const post = await dbInstance.findPostById(payload?.postId as string);
    if (!post) throw new NotFoundError();

    if (!post.likes?.find((like) => like.uid === payload.uid)) {
      delete payload.postId;
      post.likes?.push(payload);
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
  private _mapper = new PostMapper();

  public async createPost(payload: IPost) {
    const mappedPost = this._mapper.createPost(payload);
    return await dbInstance.create(mappedPost);
  }

  public async updatePost(payload: Partial<IPost>) {
    const isExist = await dbInstance.findPostById(payload?.id as string);
    if (!isExist) throw new NotFoundError("Post does not exist.");
    const mappedPayload = this._mapper.updatePost(payload);
    return await dbInstance.update(mappedPayload as IPost);
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
