import EventMapper from "../lib/mappers/event-mapper";
import EventRepository, {
  TCreateEvent,
} from "../repositories/event-repository";

class EventService {
  private _repository = new EventRepository();
  private _mapper = new EventMapper();

  public async createEvent(payload: TCreateEvent) {
    const mappedEvent = this._mapper.createEvent(payload);
    return await this._repository.create(mappedEvent);
  }

  public async getYearlyEvents(year: string, uid: string) {
    return await this._repository.getEventsCount(year, uid);
  }
}

export default EventService;
