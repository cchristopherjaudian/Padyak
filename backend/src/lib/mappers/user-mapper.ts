import { v4 as uuidv4 } from "uuid";
type TCreateUserMapper = {
  firstname: string;
  lastname: string;
  emailAddress: string;
  contactNumber: string;
  gender: string;
  birthday: string;
  photoUrl: string;
  height: string;
  weight: string;
  isAdmin: boolean;
};

class UserMapper {
  createUser(payload: TCreateUserMapper) {
    return {
      id: uuidv4(),
      createdAt: new Date().toISOString(),
      ...payload,
    };
  }
}

export default UserMapper;
