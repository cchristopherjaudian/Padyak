import { Response } from 'express';

class ResponseObject {
    public createResponse(
        res: Response,
        status: number,
        code: string,
        respObject: Record<string, any>
    ) {
        return res.status(status).json({
            status,
            code,
            data: respObject,
        });
    }
}

export default ResponseObject;
