import { IPost } from "../database/models/post";
import { NotFoundError } from "../lib/custom-errors/class-errors";
import PostRepository from "../repositories/post-repository";

class Likes {}

class PostService extends PostRepository {
  private likeInstance = new Likes();

  public async createPost(payload: IPost) {
    return await this.create(payload);
  }

  public async updatePost(payload: Partial<IPost>) {
    const isExist = await this.findPostById(payload?.uid as string);
    if (!isExist) throw new NotFoundError();
    return await this.update(payload as IPost);
  }

  public async getPosts() {
    return await this.getPostsList();
  }
}

export default PostService;
