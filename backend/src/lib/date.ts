import moment from "moment-timezone";

class DateUtils {
  private static _instance: DateUtils;
  private _defaultTz = "Asia/Manila";

  private constructor() {}

  public static getInstance(): DateUtils {
    DateUtils._instance = DateUtils._instance || new DateUtils();
    return DateUtils._instance;
  }

  public getIsoDate(date: Date) {
    return moment(date).tz(this._defaultTz).format();
  }
}

export default DateUtils;
