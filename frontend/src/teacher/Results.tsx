import React, {useContext, useEffect, useState} from "react";
import {SpecifiedTimeSlot, Student} from "../common/types";
import './Results.css';
import {AuthContext} from "../common/AuthContext";
import {BASE_URL} from "../common/Constants";

const URL = BASE_URL + "/enrollment"


const Results = () => {
  const { auth } = useContext(AuthContext);
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

  const [results, setResults] = useState([]);
  const [resultsMap, setResultsMap] = useState(new Map<SpecifiedTimeSlot, Student[]>());

  useEffect(() => {
    fetch(URL + "/results", {
      headers: {
        'Authorization': auth
      }
    })
      .then(res => res.json())
      .then((data) => {
        setResults(data);
        console.log(data);
        const newMap = new Map();
        data.forEach((result: any) => {
          const studentDto: Student[] = result['studentDto'];
          const timeslotDto: SpecifiedTimeSlot = result['timeslotDto'];
          newMap.set(timeslotDto, studentDto);
        });
        setResultsMap(newMap);
        console.log(newMap);
      })
      .catch(console.error);
  }, [setResults, auth]);

  const renderResults = () => {
    console.log(linkStatus);
    return (
      <div>
        {
          Array.from(resultsMap.entries()).map(([timeslot, students], index) => (
            <div className="timeslot" key={index}>
              <p>Weekday: {timeslot.weekday}</p>
              <p>From: {timeslot.startTime}</p>
              <p>To: {timeslot.endTime}</p>
              <ul>
                {students.map((student, index) => (
                  <li key={index}>{student.email}</li>
                ))}
              </ul>
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
    try {
      await fetch(URL + '/update', {
        method: 'PUT',
        headers: {
          'Authorization': auth
        }
      });
      alert('Success: The groups have been updated');
    } catch (error) {
      alert('Failed to update the groups');
    }
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
    return ( <p>Enrollment is not over yet. Please wait until it's closed to view the timetable.</p> );
  } else {
    return (
      <div className="container">
        <h5>Results</h5>
        {renderResults()}
        <div className="button-div">
          <button className="btn btn-secondary mt-3" onClick={exportResults}>Export to xlsx</button>
          <button className="btn btn-secondary mt-3" onClick={updateGroups}>Update Groups</button>
        </div>
      </div>
    )
  }
}

export default Results;