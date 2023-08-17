import type { Response, Request, NextFunction } from "express";
import httpStatus from "http-status";
import ResponseObject from "../lib/response-object";
import ResponseCodes from "../commons/response-codes";
import PostService from "../services/post-service";
import { IRequestWithUser } from "../middlewares/token-middleware";
import { TPostsQuery } from "../repositories/post-repository";

const postInstance = new PostService();
const responseObject = new ResponseObject();

const createPost = async (req: Request, res: Response, next: NextFunction) => {
  const request = req as IRequestWithUser;
  const { photoUrl, firstname, lastname, id } = request.user;
  try {
    const newPost = await postInstance.createPost({
      ...req.body,
      author: {
        id,
        photoUrl,
        firstname,
        lastname,
      },
    });

    responseObject.createResponse(
      res,
      httpStatus.OK,
      ResponseCodes.DATA_CREATED,
      newPost
    );
  } catch (error) {
    next(error);
  }
};

const getPosts = async (req: Request, res: Response, next: NextFunction) => {
  try {
    console.log("req.query controller", req.query);
    const posts = await postInstance.getPosts(req.query as TPostsQuery);

    responseObject.createResponse(
      res,
      httpStatus.OK,
      ResponseCodes.LIST_RETRIEVED,
      posts
    );
  } catch (error) {
    next(error);
  }
};

const updatePost = async (req: Request, res: Response, next: NextFunction) => {
  try {
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
  } catch (error) {
    next(error);
  }
};

const addLikes = async (req: Request, res: Response, next: NextFunction) => {
  const request = req as IRequestWithUser;
  try {
    const posts = await postInstance.addLikes({
      ...req.body,
      uid: request.user.id,
      displayName: `${request.user.firstname} ${request.user.lastname}`,
      photoUrl: request.user.photoUrl,
    });

    responseObject.createResponse(
      res,
      httpStatus.OK,
      ResponseCodes.DATA_MODIFIED,
      posts
    );
  } catch (error) {
    next(error);
  }
};

const addComment = async (req: Request, res: Response, next: NextFunction) => {
  const request = req as IRequestWithUser;
  try {
    const posts = await postInstance.addComment({
      ...req.body,
      userId: request.user.id,
      photoUrl: request.user.photoUrl,
      displayName: `${request.user.firstname} ${request.user.lastname}`,
    });

    responseObject.createResponse(
      res,
      httpStatus.OK,
      ResponseCodes.DATA_MODIFIED,
      posts
    );
  } catch (error) {
    next(error);
  }
};

export default { createPost, getPosts, updatePost, addLikes, addComment };
