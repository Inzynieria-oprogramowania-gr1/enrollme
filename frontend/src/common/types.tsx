export interface TimeSlot {
  start_date: string;
  end_date: string;
  is_selected: boolean;
}
export interface SpecifiedTimeSlot {
  start_date: string;
  end_date: string;
  is_selected: boolean;
  weekday: string;
}

export interface Day {
  timeSlots: TimeSlot[];
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