import winston from 'winston';

class Logger {
    private static _instance: Logger;
    private _loggerFormat: winston.Logform.Format;

    private constructor() {
        this._loggerFormat = winston.format.printf((info) => {
            if (info instanceof Error) {
                return `${info.level} - ${info.timestamp} :: [${info.name}] ${info.message} ${info.stack}`;
            }
            return `${info.level} - ${info.timestamp} :: ${info.message}`;
        });
    }

    public static getInstance(): Logger {
        Logger._instance = Logger._instance || new Logger();
        return Logger._instance;
    }

    public write() {
        const logger = winston.createLogger({
            level: 'info',
            levels: {
                info: 1,
                sql: 2,
                warn: 3,
                error: 4,
            },
            format: winston.format.combine(
                winston.format.timestamp({
                    format: 'MMM-DD-YYYY HH:mm:ss',
                }),
                this._loggerFormat
            ),
            transports: [new winston.transports.Console()],
        });

        return logger;
    }
}

export default Logger;
