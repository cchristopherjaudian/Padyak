import Firestore from '../database/firestore';
import {
  IEvent,
  IRegisteredUser,
  TRescueGroup,
} from '../database/models/event';
import { IUserModel } from '../database/models/user';

export type TCreateEvent = {
  id: string;
  month: string;
  year: string;
  eventDate: string;
  name: string;
  eventDescription: string;
  startTime: string;
  endTime: string;
  award: string;
  registeredUser?: IRegisteredUser[];
  author: Pick<IUserModel, 'id'>;
  rescueGroup: TRescueGroup[];
};

export type TEventListQuery = {
  year: string;
  month: string;
};

const arrayOfMonths = [
  'January',
  'February',
  'March',
  'April',
  'May',
  'June',
  'July',
  'August',
  'September',
  'October',
  'November',
  'December',
];

class EventRepository {
  private _colName = 'events';
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

  public async getByEventDate(date: string) {
    try {
      const event = await this._firestore
        .getDb()
        .collection(this._colName)
        .where('eventDate', '==', date)
        .get();
      return event.docs.length === 0
        ? []
        : (event.docs.map((evt) => evt.data()) as IEvent[]);
    } catch (error) {
      throw error;
    }
  }

  public async deleteEvent(id: string) {
    try {
      await this._firestore
        .setCollectionName(this._colName)
        .setDocId(id)
        .delete();

      return id;
    } catch (error) {
      throw error;
    }
  }

  public async getEventsCount(year: string) {
    const events: Record<string, number>[] = [];
    console.log('year', year);
    const eventsRef = await this._firestore
      .getDb()
      .collection(this._colName)
      .where('year', '==', year)
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

  public async getEventList(query: TEventListQuery) {
    try {
      const eventRef = await this._firestore
        .getDb()
        .collection(this._colName)
        .where('year', '==', query.year)
        .where('month', '==', query.month)
        .orderBy('createdAt', 'desc')
        .get();

      const mappedRef = eventRef.docs.map((k) => k.data());
      return mappedRef as IEvent[];
    } catch (error) {
      throw error;
    }
  }
}

export default EventRepository;
