import { Router } from "express";
import userRoutes from "./user-route";
import postRoutes from "./posts-route";

const router = Router();

type TRoute = {
  path: string;
  controller: Router;
};
type TRoutelist = TRoute[];

const defaultRoutes: TRoutelist = [
  {
    path: "/user",
    controller: userRoutes,
  },
  {
    path: "/posts",
    controller: postRoutes,
  },
];

defaultRoutes.forEach((route) => router.use(route.path, route.controller));

export default router;
