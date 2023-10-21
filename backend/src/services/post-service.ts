import { IComments, IPost } from '../database/models/post';
import { IUserModel } from '../database/models/user';
import { NotFoundError } from '../lib/custom-errors/class-errors';
import PostMapper from '../lib/mappers/post-mapper';
import PostLikesRepository, {
    TPostsQuery,
} from '../repositories/post-repository';
import UserRepository from '../repositories/user-repository';

const dbInstance = new PostLikesRepository();

type TAddLikes = {
    postId?: string;
    userId: string;
    id: string;
};

type TAddComment = {
    id: string;
    userId: string;
    postId?: string;
    comment: string;
    createdAt: string;
};

class Likes {
    public async addLikes(payload: TAddLikes) {
        try {
            const post = await dbInstance.findPostById(
                payload?.postId as string
            );
            if (!post) throw new NotFoundError();

            const hasLiked = post.likes?.find(
                (like) => like.userId === payload.userId
            );
            if (!hasLiked) {
                delete payload.postId;
                post.likes?.push(payload);
            } else {
                const removedLike = post.likes?.filter(
                    (like) => like.userId !== payload.userId
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
            const post = await dbInstance.findPostById(
                payload?.postId as string
            );
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
        const isExist = await dbInstance.findPostById(payload?.id as string);
        if (!isExist) throw new NotFoundError('Post does not exist.');
        const mappedPayload = this._mapper.updatePost(payload);
        return await dbInstance.update(mappedPayload as IPost);
    }

    public async getPosts(query: TPostsQuery) {
        const posts = await dbInstance.getPostsList(query);
        if (posts.length === 0) return posts;

        const user = new UserRepository();
        const mappedPosts = await Promise.all(
            posts.map(async (post) => {
                const author = await user.getUserById(post.author.id);

                post.author = {
                    id: author.id,
                    firstname: author.firstname as string,
                    lastname: author.lastname as string,
                    photoUrl: author.photoUrl as string,
                } as IUserModel;

                if (post!.likes!.length > 0) {
                    const likes = await Promise.all(
                        post.likes?.map(async (like) => {
                            const likedBy = await user.getUserById(
                                like?.userId as string
                            );
                            return {
                                id: like.id,
                                photoUrl: likedBy.photoUrl,
                                displayName: `${likedBy.firstname} ${likedBy.lastname}`,
                            };
                        }) as []
                    );

                    post.likes = likes as any;
                }

                if (post.comments!.length > 0) {
                    const comments = await Promise.all(
                        post.comments?.map(async (like) => {
                            const commentedBy = await user.getUserById(
                                like?.userId as string
                            );
                            return {
                                id: like.id,
                                photoUrl: commentedBy.photoUrl,
                                displayName: `${commentedBy.firstname} ${commentedBy.lastname}`,
                            };
                        }) as []
                    );

                    post.comments = comments as any;
                }

                return post;
            })
        );

        return mappedPosts;
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
