import { v4 as uuidv4 } from "uuid";
import { TCreateEvent } from "../../repositories/event-repository";
import DateUtils from "../date";

const date = DateUtils.getInstance();

class EventMapper {
  createEvent(payload: TCreateEvent) {
    return {
      ...payload,
      id: uuidv4(),
      createdAt: date.getIsoDate(new Date().toDateString()),
      modifiedAt: null,
    };
  }
}

export default EventMapper;
