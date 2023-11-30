import { IAlert } from '../database/models/alert';
import { NotFoundError } from '../lib/custom-errors/class-errors';
import UserAlertsMapper from '../lib/mappers/user-alerts-mapper';
import AlertRepository from '../repositories/alerts-repository';
import UserAlertsRepository, {
    TListUserAlerts,
    TRawSendAlert,
    TUpdateAlertStatus,
    TUserSendAlert,
} from '../repositories/user-alerts-repository';

export interface ISmsAlert {
    send: (to: string, body: string) => Promise<string>;
}

export interface IAlertService {
    getAlert: (level: number) => Promise<IAlert>;
    sendAlert: (
        sms: ISmsAlert,
        payload: Pick<TUserSendAlert, 'to'> & { message: string }
    ) => Record<string, any>;
}

class UserAlerts {
    private _alert: IAlertService;
    private _repository = new UserAlertsRepository();
    private _mapper = new UserAlertsMapper();

    constructor(alert: IAlertService) {
        this._alert = alert;
    }

    private baseMessage(
        payload: Pick<TUserSendAlert, 'displayName' | 'level'>
    ) {
        return `PADYAK ALERT: This is ${payload.displayName} I have a Level ${
            payload.level + 1
        } Emergency,`;
    }

    public async updateStatus(payload: TUpdateAlertStatus) {
        try {
            return await this._repository.update(payload);
        } catch (error) {
            throw error;
        }
    }

    public async getUserAlerts(query: TListUserAlerts) {
        try {
            return await this._repository.list(query);
        } catch (error) {
            throw error;
        }
    }

    public async sendAlert(sms: ISmsAlert, payload: TRawSendAlert) {
        try {
            const alert = await this._alert.getAlert(payload.level);
            const message = `${this.baseMessage(payload)}, ${alert.message} ${
                payload.location
            }`;

            const mappedUserAlert = await this._mapper.createUserAlert({
                to: payload.to.split(',').map((k) => '63' + k.substring(1)),
                uid: payload.uid,
                level: payload.level,
                location: payload.location,
                longitude: payload.longitude,
                latitude: payload.latitude,
                status: payload.status,
                sender: payload.sender,
            });

            await this._repository.create(mappedUserAlert);
            const alerted = await this._alert.sendAlert(sms, {
                to: payload.to.split(',').map((k) => '63' + k.substring(1)),
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
    private _sms: ISmsAlert;

    public async getAlert(level: number) {
        try {
            const alert = await this._repository.getAlert(level);
            if (!alert) throw new NotFoundError('Alert Not Found.');
            return alert;
        } catch (error) {
            throw error;
        }
    }

    public async sendAlert(
        sms: ISmsAlert,
        payload: Pick<TUserSendAlert, 'to'> & { message: string }
    ) {
        try {
            const toUsers = payload.to;
            const sent = await Promise.all(
                toUsers.map(async (user) => {
                    return await sms.send(user, payload.message);
                })
            );

            console.log('sent', sent);
            return {
                ...payload,
            };
        } catch (error) {
            throw error;
        }
    }
}

export { AlertService, UserAlerts };
