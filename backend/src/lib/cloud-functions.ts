import { initializeApp, getApps } from 'firebase-admin/app';
import { ResetValue } from 'firebase-functions/lib/common/options';
import { Expression } from 'firebase-functions/params';
import { SupportedRegion, MemoryOption } from 'firebase-functions/v2';
import * as cf from 'firebase-functions/v2/https';
import { setGlobalOptions } from 'firebase-functions/v2';

export type THttpsFunction = cf.HttpsFunction;

export interface IHttpOptions {
    omit?: boolean | Expression<boolean>;

    region?:
        | SupportedRegion
        | string
        | Array<SupportedRegion | string>
        | Expression<string>
        | ResetValue;

    cors?: string | boolean | RegExp | Array<string | RegExp>;

    memory?: MemoryOption | Expression<number> | ResetValue;

    timeoutSeconds?: number | Expression<number> | ResetValue;

    minInstances?: number | Expression<number> | ResetValue;

    maxInstances?: number | Expression<number> | ResetValue;

    concurrency?: number | Expression<number> | ResetValue;

    cpu?: number | 'gcf_gen1';
}

setGlobalOptions({ maxInstances: 10 });

class CloudFunctions {
    private _cf = cf;
    private _options = { region: 'asia-east1' } as IHttpOptions;

    withRuntime(options?: IHttpOptions): this {
        this._options ??= options as IHttpOptions;
        return this;
    }

    handlerV2(func: cf.HttpsFunction) {
        return this._cf.onRequest(this._options, func);
    }
}

export default CloudFunctions;
