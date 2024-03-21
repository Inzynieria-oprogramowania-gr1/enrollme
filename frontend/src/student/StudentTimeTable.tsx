import React, {useEffect, useState} from "react";
import './StudentTimeTable.css'

interface TimeSlot {
  start_date: string;
  end_date: string;
  is_selected: boolean;
}

interface Day {
  timeSlots: TimeSlot[];
  weekday: string;
}

interface User {
  id: null | number;
  email: string;
  isAuthenticated: boolean;
}

interface StudentTimeTableProps {
  user: User;
}

const filterSelectedSlots = (timeTableData: Day[]) => {
  return timeTableData.filter(day =>
    day.timeSlots.some(slot => slot.is_selected)
  ).map(day => ({
    ...day,
    timeSlots: day.timeSlots.filter(slot => slot.is_selected)
  }));
}

const setSlotsToFalse = (availableTimeTableData: Day[]) => {
  return availableTimeTableData.map(day => ({
    ...day,
    timeSlots: day.timeSlots.map(slot => ({
      ...slot,
      is_selected: false
    }))
  }));
}

const StudentTimeTable: React.FC<StudentTimeTableProps> = ({ user }) => {
  const [timeTableData, setTimeTableData] = useState<Day[]>([]);
  const [availableTimeTableData, setAvailableTimeTableData] = useState<Day[]>([]);

  useEffect(() => {
    fetch("http://localhost:8080/teacher/timetable")
      .then(response => response.json())
      .then(setTimeTableData)
      .catch(err => console.error(err));
  }, [setTimeTableData]);

  useEffect(() => {
    let data = filterSelectedSlots(timeTableData);
    data = setSlotsToFalse(data);
    setAvailableTimeTableData(data);
  }, [timeTableData]);

  const toggleSlotSelection = (dayIndex: number, slotIndex: number) => {
    const newTimeTableData = [...availableTimeTableData];
    newTimeTableData[dayIndex].timeSlots[slotIndex].is_selected = !newTimeTableData[dayIndex].timeSlots[slotIndex].is_selected;
    setAvailableTimeTableData(newTimeTableData);
  };

  const updateTimeTableData = () => {
    return timeTableData.map(day => {
      const availableDay = availableTimeTableData.find(d => d.weekday === day.weekday);
      if (!availableDay) return day;
      return {
        ...day,
        timeSlots: day.timeSlots.map(slot => {
          const availableSlot = availableDay.timeSlots.find(s => s.start_date === slot.start_date && s.end_date === slot.end_date);
          if (!availableSlot) return slot;
          return {
            ...slot,
            is_selected: availableSlot.is_selected
          };
        })
      };
    });
  };

  const savePreferences = () => {
    const timeTableDataToSend = updateTimeTableData();
    fetch(`http://localhost:8080/students/${user.id}/preferences`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(timeTableDataToSend),
    })
      .then(response => {
        if (!response.ok) {
          throw new Error('Failed to save preferences');
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
      {availableTimeTableData.map((day, dayIndex) =>
        day.timeSlots
          .map((slot, slotIndex) => (
            <div
              key={`${dayIndex}-${slotIndex}`}
              className={`card mb-3 ${slot.is_selected ? 'selected' : ''}`}
              onClick={() => toggleSlotSelection(dayIndex, slotIndex)}
            >
              <div className="card-body">
                <h6 className="card-title">{`${day.weekday}`}</h6>
                <p className="card-text">{`${slot.start_date} - ${slot.end_date}`}</p>
              </div>
            </div>
          ))
      )}
      <button className="btn btn-secondary" onClick={savePreferences}>Save preferences</button>
    </div>
  )
}

export default StudentTimeTable;