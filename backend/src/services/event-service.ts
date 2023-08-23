import { IEvent, IRegisteredUser } from "../database/models/event";
import { NotFoundError } from "../lib/custom-errors/class-errors";
import DateUtils from "../lib/date";
import EventMapper from "../lib/mappers/event-mapper";
import EventRepository, {
  TCreateEvent,
  TEventListQuery,
} from "../repositories/event-repository";

interface IEventService {
  createEvent: (payload: TCreateEvent) => Promise<IEvent>;
  getYearlyEvents: (
    year: string,
    uid: string
  ) => Promise<Record<string, number>[]>;
  update: (payload: Partial<TCreateEvent>) => Promise<IEvent>;
  getEvent: (id: string) => Promise<IEvent>;
}

class EventRegistration {
  private _event: IEventService;

  constructor(event: IEventService) {
    this._event = event;
  }
  public async registerCyclist(
    payload: IRegisteredUser & { eventId: string; modifiedAt: string }
  ) {
    try {
      const event = await this._event.getEvent(payload.eventId);
      if (!event) throw new NotFoundError("Event not found.");

      if (
        !event.registeredUser?.find(
          (user: IRegisteredUser) => user.user.id === payload.user.id
        )
      ) {
        event.registeredUser?.push(payload);
      }

      event.modifiedAt = payload.modifiedAt;
      return this._event.update(event);
    } catch (error) {
      throw error;
    }
  }
}

class EventService implements IEventService {
  private _repository = new EventRepository();
  private _mapper = new EventMapper();
  private _dateUtils = DateUtils.getInstance();

  public async createEvent(payload: TCreateEvent) {
    try {
      const mappedEvent = this._mapper.createEvent(payload);
      return (await this._repository.create(mappedEvent)) as IEvent;
    } catch (error) {
      throw error;
    }
  }

  public async getYearlyEvents(year: string) {
    try {
      return await this._repository.getEventsCount(year);
    } catch (error) {
      throw error;
    }
  }

  public async update(payload: Partial<TCreateEvent>) {
    try {
      await this.getEvent(payload.id as string);
      const updatedEvent = await this._repository.update(payload);
      return updatedEvent as IEvent;
    } catch (error) {
      throw error;
    }
  }

  public async getCurrentEvent() {
    return await this._repository.getByEventDate(
      this._dateUtils
        .getMomentInstance(new Date())
        .tz("Asia/Manila")
        .format("YYYY-MM-DD")
    );
  }

  public async getEvent(id: string) {
    try {
      const event = (await this._repository.findEventById(id)) as IEvent & {
        isDone: boolean;
        isNow: boolean;
      };
      if (!event) throw new NotFoundError("Event not found.");

      event.isDone = this.getEventValidity("2023-08-23T22:20:54+08:00");

      const startTime = this._dateUtils.getMomentInstance(event.startTime);
      const endTime = this._dateUtils.getMomentInstance(event.endTime);
      const current = this._dateUtils.getMomentInstance().tz("Asia/Manila");

      console.log(
        "current.isSameOrAfter(startTime)",
        current.isSameOrAfter(startTime)
      );
      console.log(
        "current.isSameOrBefore(endTime))",
        current.isSameOrBefore(endTime)
      );

      console.log("current", current);
      console.log("startTime", startTime);
      console.log("endTime", endTime);
      event.isNow =
        current.isSameOrAfter(startTime) && current.isSameOrBefore(endTime);

      return event as IEvent;
    } catch (error) {
      throw error;
    }
  }

  private getEventValidity(eventDate: string) {
    return this._dateUtils
      .getMomentInstance(new Date())
      .isAfter(new Date(eventDate));
  }

  public async deleteEvents(ids: string[]) {
    try {
      const deletedEvents = await Promise.all(
        ids.map(async (id) => {
          return await this._repository.deleteEvent(id);
        })
      );

      return deletedEvents;
    } catch (error) {
      throw error;
    }
  }

  public async getEvents(query: TEventListQuery) {
    try {
      const events = await this._repository.getEventList(query);
      const mappedEvents = events.map((event) => {
        return {
          ...event,
          isDone: this.getEventValidity(event.eventDate),
        };
      });
      return mappedEvents;
    } catch (error) {
      throw error;
    }
  }
}

export { EventService, EventRegistration };
