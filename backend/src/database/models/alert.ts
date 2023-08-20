export interface IAlert {
  id: string;
  level: 0;
  message: string;
}

export enum IAlertStatuses {
  ACTIVE = "ACTIVE",
  COMPLETED = "COMPLETED",
}
