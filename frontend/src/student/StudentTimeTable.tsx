import React, {useEffect, useState} from "react";
import './StudentTimeTable.css'
import {Day, StudentPreferenceSlot, User} from "../common/types";
import {BASE_URL} from "../common/Constants";
import {StudentPreference} from "../common/types";

const URL = BASE_URL + "/students";

interface StudentTimeTableProps {
  user: User;
}

const filterSelectedSlots = (timeTableData: Day[]) => {
  return timeTableData.map(day => ({
    ...day,
    timeslots: day.timeslots.filter(slot => slot.isSelected)
  })).filter(day => day.timeslots.length > 0);
}

const setSlotsToFalse = (availableTimeTableData: Day[]) => {
  return availableTimeTableData.map(day => ({
    ...day,
    timeslots: day.timeslots.map(slot => ({
      ...slot,
      isSelected: false
    }))
  }));
}

const fillStudentPreference = (studentPreference: StudentPreference, timeTableData: Day[]): StudentPreference => {
  const filledPreferences: StudentPreferenceSlot[] = timeTableData.flatMap(day => {
    return day.timeslots.map(slot => {
      const preference = studentPreference.preferences.find(p =>
        p.timeslot.weekday === day.weekday &&
        p.timeslot.startTime === slot.startTime &&
        p.timeslot.endTime === slot.endTime
      );
      return preference || {
        timeslot: {
          weekday: day.weekday,
          startTime: slot.startTime,
          endTime: slot.endTime
        },
        selected: false,
        note: null
      };
    });
  });

  return {
    ...studentPreference,
    preferences: filledPreferences
  };
}

const StudentTimeTable: React.FC<StudentTimeTableProps> = ({user}) => {
  const [timeTableData, setTimeTableData] = useState<Day[]>([]);
  const [studentPreference, setStudentPreference] = useState<StudentPreference>({id: 0, email: '', preferences: []});

  useEffect(() => {
    fetch(URL + "/timetable")
      .then(response => response.json())
      .then(data => {
        setTimeTableData(setSlotsToFalse(filterSelectedSlots(data)));
      })
      .catch(err => console.error(err));
  }, [setTimeTableData]);

  useEffect(() => {
    if (timeTableData.length > 0) {
      fetch(URL + `/${user.id}/preferences`)
        .then(response => response.json())
        .then(data => {
          const filledPreferences = fillStudentPreference(data, timeTableData);
          setStudentPreference(filledPreferences);
        })
        .catch(err => console.log(err));
    }
  }, [timeTableData, setStudentPreference]);

  const toggleSlotSelection = (weekday: string, startTime: string, endTime: string) => {
    const dayIndex = timeTableData.findIndex(day => day.weekday === weekday);
    if (dayIndex !== -1) {
      const slotIndex = timeTableData[dayIndex].timeslots.findIndex(slot => slot.startTime === startTime && slot.endTime === endTime);
      if (slotIndex !== -1) {
        const newPreferences = [...studentPreference.preferences];
        const preferenceIndex = newPreferences.findIndex(p =>
          p.timeslot.weekday === weekday &&
          p.timeslot.startTime === startTime &&
          p.timeslot.endTime === endTime
        );
        if (preferenceIndex !== -1) {
          newPreferences[preferenceIndex].selected = !newPreferences[preferenceIndex].selected;
          setStudentPreference({...studentPreference, preferences: newPreferences});
        }
      }
    }
  };

  const handleNoteChange = (weekday: string, startTime: string, endTime: string, note: string) => {
    const newPreferences = [...studentPreference.preferences];
    const preferenceIndex = newPreferences.findIndex(p =>
      p.timeslot.weekday === weekday &&
      p.timeslot.startTime === startTime &&
      p.timeslot.endTime === endTime
    );
    if (preferenceIndex !== -1) {
      newPreferences[preferenceIndex].note = note;
      setStudentPreference({...studentPreference, preferences: newPreferences});
    }
  };

  const savePreferences = () => {
    fetch(URL + `/${user.id}/preferences`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(studentPreference),
    })
      .then(response => {
        if (!response.ok) {
          throw new Error('Failed to save preferences' + response.json());
        }
        alert("Preferences saved successfully!")
        return response.json();
      })
      .then(data => console.log(data))
      .catch((error) => {
        alert(error);
      });
  };

  return (
    <div className="container">
      <h5 className="mb-3">Fill your preferences</h5>
      {studentPreference.preferences.map((preference, index) =>
        <div key={index} className="d-flex justify-content-between">
          <div
            className={`student-cell card mb-3 ${preference.selected ? 'selected' : ''}`}
            onClick={() => toggleSlotSelection(preference.timeslot.weekday, preference.timeslot.startTime, preference.timeslot.endTime)}
          >
            <div className="card-body">
              <h6 className="card-title">{`${preference.timeslot.weekday}`}</h6>
              <p className="card-text">{`${preference.timeslot.startTime} - ${preference.timeslot.endTime}`}</p>
            </div>
          </div>
          <div className='student-note card mb-3'>
            <textarea
              className="input card-text"
              value={preference.note || ''}
              onChange={(e) => handleNoteChange(preference.timeslot.weekday, preference.timeslot.startTime, preference.timeslot.endTime, e.target.value)}
              placeholder="Add a note"
            />
          </div>

        </div>
      )}
      <button className="btn btn-secondary" onClick={savePreferences}>Save preferences</button>
    </div>
  )
}

export default StudentTimeTable;