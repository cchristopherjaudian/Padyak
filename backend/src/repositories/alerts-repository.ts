import Firstore from "../database/firestore";
import { IAlert, IAlertStatuses } from "../database/models/alert";

class AlertRepository {
  private _colName = "alerts";
  private _firestore = Firstore.getInstance();

  public async getAlert(level: number) {
    try {
      const alertRef = await this._firestore
        .getDb()
        .collection(this._colName)
        .where("level", "==", level.toString())
        .get();

      return alertRef.docs.length > 0
        ? (alertRef.docs.pop()?.data() as IAlert)
        : null;
    } catch (error) {
      throw error;
    }
  }
}

export default AlertRepository;
