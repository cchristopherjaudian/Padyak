import { v4 as uuidv4 } from 'uuid';
import { TCreateEvent } from '../../repositories/event-repository';
import DateUtils from '../date';

const date = DateUtils.getInstance();

class EventMapper {
  createEvent(payload: TCreateEvent) {
    const mappedRescueGroup = payload.rescueGroup.map((rescuer, index) => ({
      contact: rescuer.contact,
      name: `Rescue Group ${index + 1}`,
    }));
    return {
      ...payload,
      id: uuidv4(),
      rescueGroup: mappedRescueGroup,
      createdAt: date.getIsoDate(new Date()),
      modifiedAt: null,
    };
  }
}

export default EventMapper;
