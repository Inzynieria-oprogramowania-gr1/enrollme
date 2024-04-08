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
  password: string | null;
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