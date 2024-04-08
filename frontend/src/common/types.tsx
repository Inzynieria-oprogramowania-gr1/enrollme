export const LOCAL_ENDPOINT = "http://localhost:8080"

export const RELEASE_ENDPOINT = "http://81.28.6.141:8080"

export const LOCAL_FRONT_ENDPOINT = "http://localhost:3000"

export const RELEASE_FRONT_ENDPOINT = "http://81.28.6.141:3000"

export interface EnrollConfiguration {
  deadline: string | null;
  groupAmount: number;
  id: number;
  state: string;
  timeslots: Day[];
}

export interface Day {
  timeslots: TimeSlot[];
  weekday: string;
}

export interface TimeSlot {
  endTime: string;
  isSelected: boolean;
  startTime: string;
}
export interface SpecifiedTimeSlot {
  startTime: string;
  endTime: string;
  isSelected: boolean;
  weekday: string;
}

export interface User {
  id: null | number;
  email: string;
  role: string;
  isAuthenticated: boolean;
}

export interface ShareLinkData {
  link: string;
  state: string;
}

export interface Student {
  id: number;
  email: string;
  role: string;
}