import { Router } from "express";
import userRoutes from "./user";

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
];

defaultRoutes.forEach((route) => router.use(route.path, route.controller));

export default router;
