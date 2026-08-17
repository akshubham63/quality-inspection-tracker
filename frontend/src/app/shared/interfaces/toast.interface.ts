import { TOASTER_TYPE } from "../enums/toaster-type.enum";

export interface Toast {
  id: number;
  message: string;
  type: TOASTER_TYPE;
}