import Firestore from "../database/firestore";
import { IEvent, IRegisteredUser } from "../database/models/event";

export type TCreateEvent = {
  id: string;
  month: string;
  year: string;
  eventDate: string;
  uid: string;
  name: string;
  photoUrl: string;
  registeredUser?: IRegisteredUser[];
};

const arrayOfMonths = [
  "January",
  "February",
  "March",
  "April",
  "May",
  "June",
  "July",
  "August",
  "September",
  "October",
  "November",
  "December",
];

class EventRepository {
  private _colName = "events";
  private _firestore = Firestore.getInstance();

  public async create(payload: TCreateEvent) {
    try {
      console.log("payload", payload);
      const newUser = await this._firestore
        .setCollectionName(this._colName)
        .setDocId(payload.id)
        .create(payload);
      return newUser as TCreateEvent;
    } catch (error) {
      throw error;
    }
  }

  public async getEventsCount(year: string, uid: string) {
    const events: Record<string, number>[] = [];
    const eventsRef = await this._firestore
      .getDb()
      .collection(this._colName)
      .where("uid", "==", uid)
      .where("year", "==", year)
      .get();

    if (eventsRef.docs.length === 0) return [];
    const mappedEvents = eventsRef.docs.map((event) => event.data());

    arrayOfMonths.forEach((month) => {
      events.push({
        [month]: mappedEvents.filter((evt) => evt.month === month).length,
      });
    });

    return events;
  }
}

export default EventRepository;
