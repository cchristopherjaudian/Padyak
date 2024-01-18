import {
  TCreateEmergencyContact,
  TRemoveEmergencyContact,
} from '../database/models/contacts';
import { BadRequestError } from '../lib/custom-errors/class-errors';
import UserRepository from '../repositories/user-repository';

class EmergencyContacts {
  private readonly _userRepository = new UserRepository();

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
      lastname: payload.lastname,
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
}

export { EmergencyContacts };
