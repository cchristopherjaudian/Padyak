import {
  TCreateEmergencyContact,
  TGetEmergencyContactsQry,
  TRemoveEmergencyContact,
} from '../database/models/contacts';
import { BadRequestError } from '../lib/custom-errors/class-errors';
import EventRepository from '../repositories/event-repository';
import UserRepository from '../repositories/user-repository';
import DateUtils from '../lib/date';
import { EventPaymentStatus, TRescueGroup } from '../database/models/event';

class EmergencyContacts {
  private readonly _userRepository = new UserRepository();
  private readonly _dateUtils = DateUtils.getInstance();
  public async createEmergencyContact(payload: TCreateEmergencyContact) {
    const hasUser = await this._userRepository.getUserById(payload.userId);
    if (!hasUser) throw new BadRequestError('User does not exists.');

    const hasEmergencyContact = hasUser.emergencyContacts?.find(
      (contact) => contact.contact === payload.contact
    );
    if (hasEmergencyContact) {
      throw new BadRequestError('Emergency contact already listed.');
    }

    hasUser.emergencyContacts?.push({
      firstname: payload.firstname,
      lastname: payload?.lastname,
      contact: payload.contact,
    });
    return this._userRepository.update(hasUser);
  }

  public async removeEmergencyContact(payload: TRemoveEmergencyContact) {
    const hasUser = await this._userRepository.getUserById(payload.userId);
    if (!hasUser) throw new BadRequestError('User does not exists.');

    hasUser.emergencyContacts = hasUser.emergencyContacts?.filter(
      (contact) => contact.contact !== payload.contact
    );

    return this._userRepository.update(hasUser);
  }

  public async getEmergencyContacts(query: TGetEmergencyContactsQry) {
    const user = await this._userRepository.getUserById(query.userId);

    const eventRepo = new EventRepository();
    const eventDate = this._dateUtils
      .getMomentInstance(new Date())
      .tz('Asia/Manila')
      .format('YYYY-MM-DD');
    const currentDate = this._dateUtils.getMomentInstance().tz('Asia/Manila');

    const events = await eventRepo.getByEventDate(eventDate);

    let rescueGroups = [] as TRescueGroup[];
    if (events.length > 0) {
      const filteredEvents = events.filter((event) => {
        const isPaid = event.registeredUser?.find(
          (registered) =>
            registered.status === EventPaymentStatus.PAID &&
            registered.user.id === query.userId
        );

        const startTime = this._dateUtils
          .getMomentInstance(new Date(event.startTime))
          .tz('Asia/Manila');
        const endTime = this._dateUtils
          .getMomentInstance(event.endTime)
          .tz('Asia/Manila');

        event.isDone = this._dateUtils
          .getMomentInstance(new Date())
          .tz('Asia/Manila')
          .isSameOrAfter(endTime);
        event.isNow =
          currentDate.isSameOrAfter(startTime) &&
          currentDate.isSameOrBefore(endTime);
        return isPaid && !event.isDone && event.isNow;
      });

      if (filteredEvents.length > 0) {
        rescueGroups = [
          ...filteredEvents.flatMap((event) => event.rescueGroup),
        ];
      }
    }

    return {
      emergencyContacts: user.emergencyContacts,
      rescueGroups: rescueGroups,
    };
  }
}

export { EmergencyContacts };
