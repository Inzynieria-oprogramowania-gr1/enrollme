import React, {useContext, useEffect, useState} from "react";
import {EnrollmentResultsDto, SpecifiedTimeSlot, Student, StudentPreference} from "../common/types";
import './Results.css';
import {AuthContext} from "../common/AuthContext";
import {BASE_URL} from "../common/Constants";

const URL = BASE_URL + "/enrollment"


const Results = () => {
  const {auth} = useContext(AuthContext);
  const [linkStatus, setLinkStatus] = useState<string | null>(null);

  useEffect(() => {
    fetch(URL + '/share', {
      headers: {
        'Authorization': auth
      }
    })
      .then(response => response.json())
      .then(data => setLinkStatus(data.state))
      .catch(error => setLinkStatus('NOT_STARTED'));
  }, [auth]);

  const [preferences, setPreferences] = useState<StudentPreference[]>([]);

  useEffect(() => {
    fetch(URL + "/preferences", {
      headers: {
        'Authorization': auth
      }
    })
      .then(res => res.json())
      .then((data) => {
        setPreferences(data);
      })
      .catch(console.error);
  }, []);

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedStudentPreferences, setSelectedStudentPreferences] = useState<StudentPreference | null>(null);

  const closeModal = () => {
    setIsModalOpen(false);
  };

  const handleStudentClick = (student: Student) => {
    const studentPreferences = preferences.find(preference => preference.id === student.id) || null;
    setSelectedStudentPreferences(studentPreferences);
    setIsModalOpen(true);
  };

  const [results, setResults] = useState<EnrollmentResultsDto[]>();
  const [resultsMap, setResultsMap] = useState(new Map<SpecifiedTimeSlot, Student[]>());
  const [selectedTimeslot, setSelectedTimeslot] = useState<SpecifiedTimeSlot | null>(null);
  const [previousTimeslots, setPreviousTimeslots] = useState(new Map<string, SpecifiedTimeSlot>());

  useEffect(() => {
    fetch(URL + "/results", {
      headers: {
        'Authorization': auth
      }
    })
      .then(res => res.json())
      .then((data) => {
        setResults(data);
        const newMap = new Map();
        data.forEach((result: any) => {
          const studentDto: Student[] = result['studentDto'];
          const timeslotDto: SpecifiedTimeSlot = result['timeslotDto'];
          newMap.set(timeslotDto, studentDto);
        });
        setResultsMap(newMap);
      })
      .catch(console.error);
  }, [setResults, auth]);

  const handleTimeslotChange = (event: any, student: Student) => {
    // @ts-ignore
    const selectedTimeslotKey = Array.from(resultsMap.keys())[event.target.selectedIndex];
    let map = new Map(resultsMap);
    setSelectedTimeslot(selectedTimeslotKey);
    let currentTimeslot: SpecifiedTimeSlot | undefined;
    // @ts-ignore
    for (const [key, value] of map.entries()) {
      if (value.includes(student)) {
        currentTimeslot = key;
        break;
      }
    }

    if (currentTimeslot) {
      let students = map.get(currentTimeslot);
      students = students?.filter((s) => s !== student);
      if (students) {
        map.set(currentTimeslot, students);
      }
      setResultsMap(map);
    }
    map.get(selectedTimeslotKey)?.push(student);
  }

  console.log(preferences);

  const renderModal = () => (
    <div className={`modal ${isModalOpen ? 'show' : ''}`} onClick={closeModal}>
      <div className="modal-content" onClick={e => e.stopPropagation()}>
        <h5>Student Preferences</h5>
        {selectedStudentPreferences ? (
          <div>
            <div>
              <table className="modal-table">
                <thead>
                <tr>
                  <th>Weekday</th>
                  <th>Start Time</th>
                  <th>End Time</th>
                  <th>Is Selected</th>
                  <th>Note</th>
                </tr>
                </thead>
                <tbody>
                {selectedStudentPreferences.preferences.map((preference, index) => (
                  <tr key={index}>
                    <td>{preference.timeslot.weekday}</td>
                    <td>{preference.timeslot.startTime}</td>
                    <td>{preference.timeslot.endTime}</td>
                    <td>{preference.selected ? 'Yes' : 'No'}</td>
                    <td>{preference.note || 'No note'}</td>
                  </tr>
                ))}
                </tbody>
              </table>
            </div>
          </div>
        ) : (
          <p>This student has not set any preferences.</p>
        )}
        <button className="btn btn-secondary mt-3" onClick={closeModal}>Close</button>
      </div>
    </div>
  )

  const renderResults = () => {
    return (
      <div>
        {
          Array.from(resultsMap.entries()).map(([timeslot, students], index) => (
            <div className="timeslot" key={index}>
              <div className="weekday-div">
                <p>Weekday: {timeslot.weekday}</p>
                <p>From: {timeslot.startTime}</p>
                <p>To: {timeslot.endTime}</p>
                <p>Amount of people: {students.length}</p>
              </div>
              <div className="student">
                <ul className="student-ul">
                  {students.map((student, index) => (
                    <div key={index} className="d-flex justify-content-between align-content-centert">
                      <li className="mt-2 email" onClick={() => handleStudentClick(student)}>{student.email}</li>
                      <select className="student-select" onChange={(event) => handleTimeslotChange(event, student)}>
                        {
                          Array.from(resultsMap.keys()).map((optionTimeslot, optionIndex) => {
                            const isCurrentTimeslot = resultsMap.get(optionTimeslot)?.includes(student);
                            return (
                              <option key={optionIndex} value={optionIndex} selected={isCurrentTimeslot}>
                                {`Weekday: ${optionTimeslot.weekday}, From: ${optionTimeslot.startTime}, To: ${optionTimeslot.endTime}`}
                              </option>
                            )
                          })
                        }
                      </select>
                    </div>
                  ))}
                </ul>
              </div>
            </div>
          ))
        }
      </div>
    )
  }

  const downloadFile = async () => {
    const response = await fetch(URL + "/results/xlsx", {
      headers: {
        'Authorization': auth
      },
    });

    if (!response.ok) {
      alert('Failed to download the file');
      return;
    }

    const blob = await response.blob();
    const url = window.URL.createObjectURL(blob); // Use window.URL.createObjectURL() here
    const link = document.createElement('a');
    link.href = url;
    link.download = 'results.xlsx';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }

  const updateGroups = async () => {
    const resultsArray = Array.from(resultsMap.entries()).map(([timeslot, students]) => ({
      timeslotDto: timeslot,
      studentDto: students,
    }));
    const response = await fetch(URL + '/results', {
      method: 'PATCH',
      headers: {
        'Authorization': auth,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(resultsArray)
    });
    if (!response.ok) {
      alert('Failed to update the groups');
      return;
    }
    alert('Success: The groups have been updated');
  }

  const exportResults = async () => {
    try {
      await downloadFile();
      alert('Success: The file has been downloaded');
    } catch (error) {
      alert('Failed to export the results to xlsx');
    }
  }

  if (linkStatus !== 'RESULTS_READY' && linkStatus !== 'CALCULATING') {
    return (<p>Enrollment is not over yet. Please wait until it's closed to view the timetable.</p>);
  } else {
    return (
      <div className="container">
        <h5>Results</h5>
        {renderResults()}
        {renderModal()}
        <div className="button-div">
          <button className="btn btn-secondary mt-3" onClick={exportResults}>Export to xlsx</button>
          <button className="btn btn-secondary mt-3" onClick={updateGroups}>Update Groups</button>
        </div>
      </div>
    )
  }
}

export default Results;