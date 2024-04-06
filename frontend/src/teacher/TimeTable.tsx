import React, {useState, useEffect, FC} from "react";
import "./TimeTable.css";
import {Day, EnrollConfiguration} from "../common/types";

interface TimeTableProps {
  linkStatus: string | null;
  setLinkStatus: (status: string) => void;
}

const TimeTable: FC<TimeTableProps> = ({ linkStatus, setLinkStatus }) => {
  const [enrollConfiguration, setEnrollConfiguration] = useState<EnrollConfiguration>();

  useEffect(() => {
    fetch("http://localhost:8080/enrollment")
      .then(response => response.json())
      .then(setEnrollConfiguration)
      .catch(err => console.error(err));
  }, [setEnrollConfiguration]);

  const allTimeSlots = Array.from(
    new Set(
      enrollConfiguration?.timeslots.flatMap(day =>
        day.timeslots?.map(slot => `${slot.startTime} - ${slot.endTime}`)
      )
    )
  );

  const handleCloseEnrollment = async () => {
    // const response = await fetch("http://localhost:8080/teacher/timetable/share", {
    //   method: 'PATCH',
    //   headers: {
    //     'Content-Type': 'application/json',
    //   },
    //   body: JSON.stringify({state: 'CALCULATING'}),
    // });
    // if (!response.ok) {
    //   alert('Failed to close the enrollment');
    //   return;
    // }
    // setLinkStatus('CALCULATING');
    // alert('Success: Enrollment was successfully closed');
  }

  const toggleTimeSlotSelection = (weekday: string, slot: string) => {
    if (linkStatus == 'CALCULATING' || linkStatus == 'ACTIVE' || linkStatus == 'RESULTS_READY') {
      return;
    }
    setEnrollConfiguration(prevData => {
      if (!prevData) return prevData;
      return {
        ...prevData,
        timeslots: prevData.timeslots.map(day => {
          if (day.weekday === weekday) {
            return {
              ...day,
              timeslots: day.timeslots.map(s => {
                if (`${s.startTime} - ${s.endTime}` === slot) {
                  return {...s, isSelected: !s.isSelected};
                }
                return s;
              }),
            };
          }
          return day;
        }),
      };
    });
  };

  const hasSelectedSlot = () => {
    return enrollConfiguration?.timeslots.some(day => day.timeslots.some(slot => slot.isSelected));
  };

  const saveTimeTable = () => {
    if (!hasSelectedSlot()) {
      alert('Please select at least one time slot');
      return;
    }
    fetch("http://localhost:8080/teacher/timetable", {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(enrollConfiguration),
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
          {enrollConfiguration?.timeslots.map((day, index) => (
            <th key={index} className="timeTable-headerCell">{day.weekday}</th>
          ))}
        </tr>
        </thead>
        <tbody>
        {allTimeSlots.map((slot, index) => (
          <tr key={index} className="timeTable-row">
            <td>{slot}</td>
            {enrollConfiguration?.timeslots.map((day, i) => {
              const isSelected = day.timeslots?.some(
                s => `${s.startTime} - ${s.endTime}` === slot
                  && s.isSelected
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