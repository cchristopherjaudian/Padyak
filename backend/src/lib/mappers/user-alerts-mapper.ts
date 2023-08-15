import { v4 as uudiv4 } from "uuid";
import { IUserAlerts } from "../../database/models/user-alerts";
import DateUtils from "../date";

const date = DateUtils.getInstance();
class UserAlertsMapper {
  public createUserAlert(
    payload: Omit<IUserAlerts, "id" | "createdAt" | "modifiedAt ">
  ) {
    return {
      id: uudiv4(),
      createdAt: date.getIsoDate(new Date()),
      modifiedAt: "",
      ...payload,
    };
  }
}

export default UserAlertsMapper;
