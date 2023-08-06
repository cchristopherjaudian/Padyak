import type { Response, Request, NextFunction } from "express";
import { UserService } from "../services/user-service";

const userInstance = new UserService();

const createUser = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const newUser = await userInstance.createUser(req.body);
    return res.status(200).json(newUser);
  } catch (error) {
    next(error);
  }
};

export { createUser };
