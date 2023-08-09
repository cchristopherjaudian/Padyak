import { v4 as uuidv4 } from "uuid";
import { TCreateEvent } from "../../repositories/event-repository";

class EventMapper {
  createEvent(payload: TCreateEvent) {
    return {
      ...payload,
      id: uuidv4(),
      createdAt: new Date().toISOString(),
      modifiedAt: null,
    };
  }
}

export default EventMapper;
