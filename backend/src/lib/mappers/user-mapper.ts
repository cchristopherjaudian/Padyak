import { v4 as uuidv4 } from "uuid";
import DateUtils from "../date";

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

const date = DateUtils.getInstance();
class UserMapper {
  createUser(payload: TCreateUserMapper) {
    return {
      id: uuidv4(),
      createdAt: date.getIsoDate(new Date()),
      ...payload,
    };
  }
}

export default UserMapper;
