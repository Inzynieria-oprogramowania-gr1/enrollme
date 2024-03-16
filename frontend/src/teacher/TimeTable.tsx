import React, {useState, useEffect} from "react";
import "./TimeTable.css";
import MailInputs from "./MailInputs";

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

  return (
    <div>
      <h1>Time Table</h1>
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
            {timeTableData.map((day, i) => (
              <td key={i} className="timeTable-cell">
                {day.timeSlots.some(
                  s => `${s.start_date} - ${s.end_date}` === slot
                    && s.is_selected
                )
                  ? "✓"
                  : "×"}
              </td>
            ))}
          </tr>
        ))}
        </tbody>
      </table>
      <MailInputs></MailInputs>
    </div>
  );
};

export default TimeTable;