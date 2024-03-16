import React, {useState, useEffect} from "react";
import "./TimeTable.css";
import MailInputs from "./MailInputs";
import 'bootstrap/dist/css/bootstrap.min.css';

interface TimeSlot {
  start_date: string;
  end_date: string;
  is_selected: boolean;
}

interface Day {
  timeSlots: TimeSlot[];
  weekday: string;
}

const TimeTable = () => {
  const [timeTableData, setTimeTableData] = useState<Day[]>([]);

  useEffect(() => {
    fetch("http://localhost:8080/teacher/timetable")
      .then(response => response.json())
      .then(setTimeTableData)
      .catch(err => console.error(err));
  }, [setTimeTableData]);

  const allTimeSlots = Array.from(
    new Set(
      timeTableData.flatMap(day =>
        day.timeSlots.map(slot => `${slot.start_date} - ${slot.end_date}`)
      )
    )
  );

  const toggleTimeSlotSelection = (weekday: string, slot: string) => {
    setTimeTableData(prevData =>
      prevData.map(day => {
        if (day.weekday === weekday) {
          day.timeSlots = day.timeSlots.map(s => {
            if (`${s.start_date} - ${s.end_date}` === slot) {
              return {...s, is_selected: !s.is_selected};
            }
            return s;
          });
        }
        return day;
      })
    );
  };

  const saveTimeTable = () => {
    fetch("http://localhost:8080/teacher/timetable", {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(timeTableData),
    })
      .then(response => {
        if (!response.ok) {
          throw new Error("Could not save the updated timetable")
        }
        return response.json();
      })
      .then(() => alert('Success: Timetable saved successfully'))
      .catch((error) => alert(error));
  };

  const renderTimeTable = () => (
    <table className="timeTable">
      <thead>
      <tr className="timeTable-headerRow">
        <th>Timeslots</th>
        {timeTableData.map((day, index) => (
          <th key={index} className="timeTable-headerCell">{day.weekday}</th>
        ))}
      </tr>
      </thead>
      <tbody>
      {allTimeSlots.map((slot, index) => (
        <tr key={index} className="timeTable-row">
          <td>{slot}</td>
          {timeTableData.map((day, i) => {
            const isSelected = day.timeSlots.some(
              s => `${s.start_date} - ${s.end_date}` === slot
                && s.is_selected
            );
            return (
              <td
                key={i}
                className={`timeTable-cell ${isSelected ? 'timeTable-selected' : ''}`}
                onClick={() => toggleTimeSlotSelection(day.weekday, slot)}
              >
                {isSelected ? "✓" : "×"}
              </td>
            );
          })}
        </tr>
      ))}
      </tbody>
    </table>
  );

  return (
    <div className="container">
      <h1 className="mb-3">Time Table</h1>
      <div className="mb-3">
        {renderTimeTable()}
      </div>
      <button className="btn btn-primary" onClick={saveTimeTable}>Save</button>
      <MailInputs></MailInputs>
    </div>
  );
};
export default TimeTable;