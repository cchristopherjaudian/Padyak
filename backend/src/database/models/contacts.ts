export type TEmergencyContact = {
  firstname: string;
  lastname?: string | null;
  contact: string;
};

export type TCreateEmergencyContact = TEmergencyContact & {
  userId: string;
};

export type TRemoveEmergencyContact = Pick<TEmergencyContact, 'contact'> & {
  userId: string;
};

export type TGetEmergencyContactsQry = {
  userId: string;
};
