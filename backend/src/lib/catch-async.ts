import type { NextFunction, Request, Response } from 'express';

type TCatchAsync = (req: Request, res: Response, next: NextFunction) => void;

const catchAsync =
    (fn: TCatchAsync) => (req: Request, res: Response, next: NextFunction) => {
        Promise.resolve(fn(req, res, next)).catch((err) => next(err));
    };

export { catchAsync };
