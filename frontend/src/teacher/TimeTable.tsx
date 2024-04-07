import React, {useState, useEffect, FC} from "react";
import "./TimeTable.css";
import {EnrollConfiguration} from "../common/types";

const ENDPOINT = "http://localhost:8080/enrollment";

interface TimeTableProps {
  linkStatus: string | null;
  setLinkStatus: (status: string) => void;
}

const TimeTable: FC<TimeTableProps> = ({linkStatus, setLinkStatus}) => {
  const [enrollConfiguration, setEnrollConfiguration] = useState<EnrollConfiguration>();
  const [groupAmount, setGroupAmount] = useState<number>(0);
  const [deadline, setDeadline] = useState<string | null>(null);

  useEffect(() => {
    fetch(ENDPOINT)
      .then(response => response.json())
      .then(data => {
        setEnrollConfiguration(data);
        setGroupAmount(data.groupAmount);
        setDeadline(data.deadline);
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

  const handleDeadlineChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    console.log(deadline)
    setDeadline(event.target.value as string);
  };

  const handleNoDeadlineChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setDeadline(event.target.checked ? null : new Date().toISOString().split('.')[0]);
  };

  const handleSaveConfiguration = async () => {
    if (groupAmount > selectedSlotsCount()) {
      alert('Group amount cannot be greater than the number of selected slots');
      setGroupAmount(enrollConfiguration?.groupAmount ?? 0);
      return;
    }
    if (deadline && new Date(deadline) < new Date()) {
      alert('Deadline cannot be set in the past');
      return;
    }
    const dataToSend = {
      id: enrollConfiguration?.id,
      groupAmount: groupAmount,
      deadline: deadline ? formatDate(deadline) : null
    };
    const response = await fetch(ENDPOINT + "/config", {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(dataToSend),
    });
    if (!response.ok) {
      alert('Failed to save the configuration');
      return;
    }
    setEnrollConfiguration(prevConfig => {
      if (!prevConfig) return prevConfig;
      return {
        ...prevConfig,
        groupAmount: groupAmount,
        deadline: deadline
      };
    });
    alert('Success: Configuration was successfully saved');
  }

  const formatDate = (dateStr: string) => {
    const date = new Date(dateStr);
    const year = date.getFullYear();
    const month = ('0' + (date.getMonth() + 1)).slice(-2);
    const day = ('0' + date.getDate()).slice(-2);
    const hours = ('0' + date.getHours()).slice(-2);
    const minutes = ('0' + date.getMinutes()).slice(-2);
    const seconds = ('0' + date.getSeconds()).slice(-2);
    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
  }

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

  const selectedSlotsCount = () => {
    let count = 0;
    enrollConfiguration?.timeslots.forEach(day => {
      day.timeslots.forEach(slot => {
        if (slot.isSelected) {
          count++;
        }
      });
    });
    return count;
  };

  const saveTimeTable = () => {
    if (selectedSlotsCount() < groupAmount) {
      alert('Please select at least as many time slots as the group amount');
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
      <h5 className="mb-4 mt-5">Configure enroll details: </h5>
      <div className="d-flex justify-content-between align-items-center">
        <div className="form-group">
          <label className="form-label" htmlFor="groupAmount">Desired groups number</label>
          <input className="form-control" id="groupAmount" type="number" min="1" max="35" value={groupAmount}
                 disabled={linkStatus === 'ACTIVE' || linkStatus === 'CALCULATING' || linkStatus === 'RESULTS_READY'}
                 onChange={handleGroupAmountChange}/>
          <label>Between 1 and 35</label>
        </div>
        <div>
          <div className="form-group">
            <label className="form-label" htmlFor="deadline">Enroll close date</label>
            <input className="form-control" id="deadline" type="datetime-local" value={deadline ? deadline : ''}
                   onChange={handleDeadlineChange}
                   disabled={linkStatus === 'CALCULATING' || linkStatus === 'RESULTS_READY' ||
                     (document.getElementById('noDeadline') as HTMLInputElement)?.checked}/>
          </div>
          <div className="form-check">
            <input className="form-check-input" id="noDeadline" type="checkbox" checked={deadline === null}
                   onChange={handleNoDeadlineChange}
                   disabled={linkStatus === 'CALCULATING' || linkStatus === 'RESULTS_READY'}/>
            <label className="form-check-label" htmlFor="noDeadline">No deadline</label>
          </div>
        </div>
        <div>
          <button className="btn btn-primary" onClick={handleSaveConfiguration}
                  disabled={linkStatus === 'CALCULATING' || linkStatus === 'RESULTS_READY'}>Save enrollment details
          </button>
        </div>
        <div>
          <button className="btn btn-danger" onClick={handleCloseEnrollment}
                  disabled={linkStatus != 'ACTIVE'}>Close enrollment now
          </button>
        </div>

      </div>
    </div>
  );
};

export default TimeTable;