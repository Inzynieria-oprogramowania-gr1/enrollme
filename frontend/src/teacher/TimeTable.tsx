import React, {useState, useEffect, FC} from "react";
import "./TimeTable.css";
import {Day, EnrollConfiguration} from "../common/types";

const ENDPOINT = "http://localhost:8080/enrollment";

interface TimeTableProps {
  linkStatus: string | null;
  setLinkStatus: (status: string) => void;
}

const TimeTable: FC<TimeTableProps> = ({linkStatus, setLinkStatus}) => {
  const [enrollConfiguration, setEnrollConfiguration] = useState<EnrollConfiguration>();
  const [groupAmount, setGroupAmount] = useState<number>(0);

  useEffect(() => {
    fetch(ENDPOINT)
      .then(response => response.json())
      .then(data => {
        setEnrollConfiguration(data);
        setGroupAmount(data.groupAmount);
      })
      .catch(err => console.error(err));
  }, [setEnrollConfiguration]);

  const allTimeSlots = Array.from(
    new Set(
      enrollConfiguration?.timeslots.flatMap(day =>
        day.timeslots?.map(slot => `${slot.startTime} - ${slot.endTime}`)
      )
    )
  );

  const handleGroupAmountChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setGroupAmount(Number(event.target.value));
  };

  const handleCloseEnrollment = async () => {
    const response = await fetch(ENDPOINT + "/share", {
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
    fetch(ENDPOINT + "/timetable", {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(enrollConfiguration?.timeslots),
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
      <h5 className="mb-2">Choose preferred time slots:</h5>
      {renderTimeTable()}
      <div className="d-flex justify-content-between">
        <button className="btn btn-secondary mt-3" onClick={saveTimeTable}
                disabled={linkStatus === 'ACTIVE' || linkStatus === 'CALCULATING' || linkStatus === 'RESULTS_READY'}>
          Save preferred slots
        </button>
      </div>
      <h5 className="mb-2 mt-5">Configure enroll details: </h5>
      <div className="d-flex justify-content-between">
        <div className="form-group">
          <label className="form-label" htmlFor="groupAmount">Desired groups number</label>
          <input className="form-control" id="groupAmount" type="number" min="1" max="35" value={groupAmount}
                 onChange={handleGroupAmountChange}/>
        </div>
        <div>
          <button className="btn btn-danger mt-3" onClick={handleCloseEnrollment}
                  disabled={linkStatus != 'ACTIVE'}>Close enrollment now
          </button>
        </div>

      </div>
    </div>
  );
};

export default TimeTable;