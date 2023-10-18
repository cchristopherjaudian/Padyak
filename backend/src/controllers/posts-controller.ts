import type { Response, Request, NextFunction } from 'express';
import httpStatus from 'http-status';
import ResponseObject from '../lib/response-object';
import ResponseCodes from '../commons/response-codes';
import PostService from '../services/post-service';
import { IRequestWithUser } from '../middlewares/token-middleware';
import { TPostsQuery } from '../repositories/post-repository';
import { catchAsync } from '../lib/catch-async';

const postInstance = new PostService();
const responseObject = new ResponseObject();

const createPost = catchAsync(
    async (req: Request, res: Response, next: NextFunction) => {
        const request = req as IRequestWithUser;
        const { id } = request.user;
        const newPost = await postInstance.createPost({
            ...req.body,
            author: {
                id,
            },
        });

        responseObject.createResponse(
            res,
            httpStatus.OK,
            ResponseCodes.DATA_CREATED,
            newPost
        );
    }
);

const getPosts = catchAsync(
    async (req: Request, res: Response, next: NextFunction) => {
        const posts = await postInstance.getPosts(req.query as TPostsQuery);

        responseObject.createResponse(
            res,
            httpStatus.OK,
            ResponseCodes.LIST_RETRIEVED,
            posts
        );
    }
);

const updatePost = catchAsync(
    async (req: Request, res: Response, next: NextFunction) => {
        const posts = await postInstance.updatePost({
            ...req.body,
            id: req.params.postId,
        });

        responseObject.createResponse(
            res,
            httpStatus.OK,
            ResponseCodes.DATA_MODIFIED,
            posts
        );
    }
);

const addLikes = catchAsync(
    async (req: Request, res: Response, next: NextFunction) => {
        const request = req as IRequestWithUser;
        const posts = await postInstance.addLikes({
            ...req.body,
            userId: request.user.id,
        });

        responseObject.createResponse(
            res,
            httpStatus.OK,
            ResponseCodes.DATA_MODIFIED,
            posts
        );
    }
);

const addComment = catchAsync(
    async (req: Request, res: Response, next: NextFunction) => {
        const request = req as IRequestWithUser;
        const posts = await postInstance.addComment({
            ...req.body,
            userId: request.user.id,
        });

        responseObject.createResponse(
            res,
            httpStatus.OK,
            ResponseCodes.DATA_MODIFIED,
            posts
        );
    }
);

export default { createPost, getPosts, updatePost, addLikes, addComment };
