import { Vonage } from "@vonage/server-sdk";
import type { ISmsAlert } from "./alert-serivce";
// Or if standalone
const { Auth } = require("@vonage/auth");

class VonageSMS implements ISmsAlert {
  private static _instance: VonageSMS;
  private _auth: typeof Auth;
  private _vonage: Vonage;
  private _from = "Padyak SMS Alert";

  private constructor() {
    this._auth = new Auth({
      apiKey: "49155345",
      apiSecret: "RyafwXGGBkl99JjE",
    });
    if (this._auth) this._vonage = new Vonage(this._auth);
  }

  public static getInstance(): VonageSMS {
    VonageSMS._instance = VonageSMS._instance || new VonageSMS();
    return VonageSMS._instance;
  }

  public async send(to: string, body: string) {
    try {
      await this._vonage.sms.send({
        from: this._from,
        to,
        text: body,
      });

      return to;
    } catch (error) {
      throw error;
    }
  }
}

export { VonageSMS };
