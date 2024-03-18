import React, {useEffect, useState} from "react";

interface TimeSlot {
  start_date: string;
  end_date: string;
  is_selected: boolean;
}

interface Day {
  timeSlots: TimeSlot[];
  weekday: string;
}

const StudentTimeTable = () => {
  const [timeTableData, setTimeTableData] = useState<Day[]>([]);

  useEffect(() => {
    fetch("http://localhost:8080/teacher/timetable")
      .then(response => response.json())
      .then(setTimeTableData)
      .catch(err => console.error(err));
  }, [setTimeTableData]);

  const availableTimeTableData = timeTableData.filter(day =>
    day.timeSlots.some(slot => slot.is_selected)
  ).map(day => ({
    ...day,
    timeSlots: day.timeSlots.filter(slot => slot.is_selected)
  }));

  return (
    <div className="container">
      <h5 className="mb-3">Fill your preferences</h5>
      {timeTableData.map((day, index) =>
        day.timeSlots
          .filter(slot => slot.is_selected)
          .map((slot, i) => (
            <div className="card mb-3" key={`${index}-${i}`}>
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