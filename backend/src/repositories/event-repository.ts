import Firestore from "../database/firestore";
import { IEvent, IRegisteredUser } from "../database/models/event";
import { IUserModel } from "../database/models/user";

export type TCreateEvent = {
  id: string;
  month: string;
  year: string;
  eventDate: string;
  name: string;
  photoUrl: string;
  registeredUser?: IRegisteredUser[];
  author: IUserModel;
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
      const newUser = await this._firestore
        .setCollectionName(this._colName)
        .setDocId(payload.id)
        .create(payload);
      return newUser as TCreateEvent;
    } catch (error) {
      throw error;
    }
  }

  public async update(payload: Partial<TCreateEvent>) {
    try {
      const event = await this._firestore
        .setCollectionName(this._colName)
        .setDocId(payload.id as string)
        .update(payload);

      return event ? (event as IEvent) : null;
    } catch (error) {
      throw error;
    }
  }

  public async findEventById(id: string) {
    return (await this._firestore
      .setCollectionName(this._colName)
      .findById(id)) as IEvent;
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
