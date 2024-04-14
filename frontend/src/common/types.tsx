export interface EnrollConfiguration {
  deadline: string | null;
  groupAmount: number;
  id: number;
  state: string;
  timeslots: Day[];
}

export interface StudentPreference {
  id: number | null;
  email: string;
  preferences: StudentPreferenceSlot[]
}

export interface StudentPreferenceSlot {
  timeslot: StudentTimeSlot;
  selected: boolean;
  note: string | null;
}

export interface StudentTimeSlot {
  weekday: string;
  startTime: string;
  endTime: string
}

export interface Day {
  timeslots: TimeSlot[];
  weekday: string;
}
export interface EnrollmentResultsDto {
  specifiedTimeSlot: SpecifiedTimeSlot;
  studentDto: Student[];
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