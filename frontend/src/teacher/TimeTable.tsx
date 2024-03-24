import React, {useState, useEffect, FC} from "react";
import "./TimeTable.css";
import {Day} from "../common/types";

interface TimeTableProps {
  linkStatus: string | null;
  setLinkStatus: (status: string) => void;
}

const TimeTable: FC<TimeTableProps> = ({ linkStatus, setLinkStatus }) => {
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

  const handleCloseEnrollment = async () => {
    const response = await fetch("http://localhost:8080/teacher/timetable/share", {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({state: 'CALCULATING'}),
    });
    if (!response.ok) {
      alert('Failed to close the enrollment');
      return;
    }
    setLinkStatus('CALCULATING');
    alert('Success: Enrollment was successfully closed');
  }

  const toggleTimeSlotSelection = (weekday: string, slot: string) => {
    if (linkStatus == 'CALCULATING') {
      return;
    }
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
    <div className="mb-3">
      {renderTimeTable()}
      <div className="d-flex justify-content-between">
        <button className="btn btn-secondary mt-3" onClick={saveTimeTable} disabled={linkStatus === 'ACTIVE' || linkStatus === 'CALCULATING' || linkStatus === 'RESULTS_READY'}>Save preferred slots</button>
        <button className="btn btn-danger mt-3" onClick={handleCloseEnrollment} disabled={linkStatus != 'ACTIVE'}>Close enrollment</button>
      </div>

    </div>
  );
};

export default TimeTable;