import { v4 as uudiv4 } from "uuid";
import { IUserAlerts } from "../../database/models/user-alerts";

class UserAlertsMapper {
  public createUserAlert(
    payload: Omit<IUserAlerts, "id" | "createdAt" | "modifiedAt ">
  ) {
    return {
      id: uudiv4(),
      createdAt: new Date().toISOString(),
      modifiedAt: "",
      ...payload,
    };
  }
}

export default UserAlertsMapper;
