import moment from "moment-timezone";

class DateUtils {
  private static _instance: DateUtils;
  private _defaultTz = "Asia/Manila";

  private constructor() {}

  public static getInstance(): DateUtils {
    DateUtils._instance = DateUtils._instance || new DateUtils();
    return DateUtils._instance;
  }

  public getIsoDate(date: string) {
    return moment.tz(date, this._defaultTz).format();
  }
}

export default DateUtils;
