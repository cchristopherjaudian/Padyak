import { IAlert } from "../database/models/alert";
import { NotFoundError } from "../lib/custom-errors/class-errors";
import UserAlertsMapper from "../lib/mappers/user-alerts-mapper";
import AlertRepository from "../repositories/alerts-repository";
import UserAlertsRepository, {
  TRawSendAlert,
  TUserSendAlert,
} from "../repositories/user-alerts-repository";

export interface IAlertService {
  getAlert: (level: number) => Promise<IAlert>;
  sendAlert: (
    payload: Pick<TUserSendAlert, "to"> & { message: string }
  ) => Promise<Pick<TUserSendAlert, "to"> & { message: string }>;
}

class UserAlerts {
  private _alert: IAlertService;
  private _repository = new UserAlertsRepository();
  private _mapper = new UserAlertsMapper();

  constructor(alert: IAlertService) {
    this._alert = alert;
  }

  private baseMessage(payload: Pick<TUserSendAlert, "displayName" | "level">) {
    return `PADYAK ALERT: This is ${payload.displayName} I have a Level ${
      payload.level + 1
    } Emergency,`;
  }

  public async sendAlert(payload: TRawSendAlert) {
    try {
      const alert = await this._alert.getAlert(payload.level);
      const message = `${this.baseMessage(payload)}, ${alert.message} ${
        payload.location
      }`;

      const mappedUserAlert = await this._mapper.createUserAlert({
        to: payload.to.split(","),
        uid: payload.uid,
        level: payload.level,
        location: payload.location,
      });

      await this._repository.create(mappedUserAlert);
      const alerted = await this._alert.sendAlert({
        to: payload.to.split(","),
        message: message,
      });

      return alerted;
    } catch (error) {
      throw error;
    }
  }
}

class AlertService implements IAlertService {
  private _repository = new AlertRepository();

  public async getAlert(level: number) {
    try {
      const alert = await this._repository.getAlert(level);
      if (!alert) throw new NotFoundError("Alert Not Found.");
      return alert;
    } catch (error) {
      throw error;
    }
  }

  public async sendAlert(
    payload: Pick<TUserSendAlert, "to"> & { message: string }
  ) {
    try {
      return {
        ...payload,
        to: payload.to,
      };
    } catch (error) {
      throw error;
    }
  }
}

export { AlertService, UserAlerts };
