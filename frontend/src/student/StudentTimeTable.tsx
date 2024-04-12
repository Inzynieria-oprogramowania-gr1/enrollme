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

const StudentTimeTable: React.FC<StudentTimeTableProps> = ({ user }) => {
  const [timeTableData, setTimeTableData] = useState<Day[]>([]);

  useEffect(() => {
    fetch(URL + "/timetable")
      .then(response => response.json())
      .then(data => {
        setTimeTableData(setSlotsToFalse(filterSelectedSlots(data)));
      })
      .catch(err => console.error(err));
  }, [setTimeTableData]);

  const toggleSlotSelection = (dayIndex: number, slotIndex: number) => {
    const newTimeTableData = [...timeTableData];
    newTimeTableData[dayIndex].timeslots[slotIndex].isSelected = !newTimeTableData[dayIndex].timeslots[slotIndex].isSelected;
    setTimeTableData(newTimeTableData);
    console.log(timeTableData);
  };

  const getDataToSend = (): StudentPreference => {
    const preferences: StudentPreferenceSlot[] = timeTableData.flatMap(day => {
      const availableDay = timeTableData.find(d => d.weekday === day.weekday);
      if (!availableDay) return [];
      return availableDay.timeslots.filter(slot => slot.isSelected).map(slot => ({
        timeslot: {
          weekday: day.weekday,
          startTime: slot.startTime,
          endTime: slot.endTime
        },
        selected: slot.isSelected,
        note: null
      }));
    });

    return {
      id: user.id,
      email: user.email,
      preferences: preferences
    };
  };

  const savePreferences = () => {
    const dataToSend = getDataToSend();
    console.log(dataToSend)
    fetch(URL + `/${user.id}/preferences`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(dataToSend),
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
      {timeTableData.map((day, dayIndex) =>
        day.timeslots
          .map((slot, slotIndex) => (
            <div
              key={`${dayIndex}-${slotIndex}`}
              className={`student-cell card mb-3 ${slot.isSelected ? 'selected' : ''}`}
              onClick={() => toggleSlotSelection(dayIndex, slotIndex)}
            >
              <div className="card-body">
                <h6 className="card-title">{`${day.weekday}`}</h6>
                <p className="card-text">{`${slot.startTime} - ${slot.endTime}`}</p>
              </div>
            </div>
          ))
      )}
      <button className="btn btn-secondary" onClick={savePreferences}>Save preferences</button>
    </div>
  )
}

export default StudentTimeTable;