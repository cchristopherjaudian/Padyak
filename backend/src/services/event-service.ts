import { IEvent, IRegisteredUser } from "../database/models/event";
import { NotFoundError } from "../lib/custom-errors/class-errors";
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
          (user: IRegisteredUser) => user.uid === payload.uid
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

  public async getEvent(id: string) {
    try {
      const event = await this._repository.findEventById(id);
      if (!event) throw new NotFoundError("Event not found.");
      return event as IEvent;
    } catch (error) {
      throw error;
    }
  }

  public async getEvents(query: TEventListQuery) {
    try {
      return await this._repository.getEventList(query);
    } catch (error) {
      throw error;
    }
  }
}

export { EventService, EventRegistration };
