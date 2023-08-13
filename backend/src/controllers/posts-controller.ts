import type { Response, Request, NextFunction } from "express";
import httpStatus from "http-status";
import ResponseObject from "../lib/response-object";
import ResponseCodes from "../commons/response-codes";
import PostService from "../services/post-service";
import { IRequestWithUser } from "../middlewares/token-middleware";
import Logger from "../commons/logger";

const postInstance = new PostService();
const responseObject = new ResponseObject();
const logger = Logger.getInstance();

const createPost = async (req: Request, res: Response, next: NextFunction) => {
  logger.write.debug("Initializing {createPost} controller...");
  const request = req as IRequestWithUser;
  try {
    const newPost = await postInstance.createPost({
      ...req.body,
      uid: request.user.id,
    });

    responseObject.createResponse(
      res,
      httpStatus.OK,
      ResponseCodes.DATA_CREATED,
      newPost
    );
  } catch (error) {
    logger.write.error(error);
    next(error);
  }
};

const getPosts = async (req: Request, res: Response, next: NextFunction) => {
  logger.write.debug("Initializing {getPosts} controller...");
  try {
    const posts = await postInstance.getPosts();

    responseObject.createResponse(
      res,
      httpStatus.OK,
      ResponseCodes.LIST_RETRIEVED,
      posts
    );
  } catch (error) {
    logger.write.error(error);
    next(error);
  }
};

const updatePost = async (req: Request, res: Response, next: NextFunction) => {
  logger.write.debug("Initializing {updatePost} controller...");
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
    logger.write.error(error);
    next(error);
  }
};

const addLikes = async (req: Request, res: Response, next: NextFunction) => {
  logger.write.debug("Initializing {addLikes} controller...");
  const request = req as IRequestWithUser;
  try {
    const posts = await postInstance.addLikes({
      ...req.body,
      userId: request.user.id,
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
    logger.write.error(error);
    next(error);
  }
};

const addComment = async (req: Request, res: Response, next: NextFunction) => {
  logger.write.debug("Initializing {addComment} controller...");
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
    logger.write.error(error);
    next(error);
  }
};

export default { createPost, getPosts, updatePost, addLikes, addComment };
