import { Router } from "express";
import userRoutes from "./user-route";
import postRoutes from "./posts-route";
import eventRoutes from "./event-route";
import adminRoutes from "./admin-route";

const router = Router();

type TRoute = {
  path: string;
  controller: Router;
};
type TRoutelist = TRoute[];

const defaultRoutes: TRoutelist = [
  {
    path: "/users",
    controller: userRoutes,
  },
  {
    path: "/posts",
    controller: postRoutes,
  },
  {
    path: "/events",
    controller: eventRoutes,
  },
  {
    path: "/admins",
    controller: adminRoutes,
  },
];

defaultRoutes.forEach((route) => router.use(route.path, route.controller));

export default router;
