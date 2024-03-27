export interface TimeSlot {
  start_date: string;
  end_date: string;
  is_selected: boolean;
}

export interface Day {
  timeSlots: TimeSlot[];
  weekday: string;
}

export interface User {
  id: null | number;
  email: string;
  isAuthenticated: boolean;
}