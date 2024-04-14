package com.company.project.dto.preferences;

public record SinglePreference(PreferredTimeslot timeslot, boolean selected, String note) {
}


// TODO remove comments
// jak przesłać preferencje??

// StudentPreferencesDto (Long id, String email, List<SinglePreferenceDto> preferences)
// SinglePreferenceDto (PreferredTimeSlot timeslot, boolean selected, String note)
// PreferredTimeSlot (Weekday weekday, LocalTime startTime, LocalTime endTime)

/*
*
* { student_id: 1,
*   email: "example@gmail.com",
*   preferences: [
*     {
*       timeslot: {
*         weekday: "Monday",
*         start_time: "08:00",
*         end_time: "09:30",
*       },
*       selected: true,
*       note: "It is ok, but I prefer another one"
*     },
*     ...
*     {...}
*   ]
*
*
*
* */