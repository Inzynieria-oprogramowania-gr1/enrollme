export interface EnrollConfiguration {
  deadline: null;
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
  start_date: string;
  end_date: string;
  is_selected: boolean;
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