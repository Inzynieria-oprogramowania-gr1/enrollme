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

const StudentTimeTable = () => {
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
                <p className="card-title">{`${day.weekday} ${slot.start_date} - ${slot.end_date}`}</p>
                <p className="card-text">{`${slot.start_date} - ${slot.end_date}`}</p>
              </div>
            </div>
          ))
      )}
    </div>
  )
}

export default StudentTimeTable;