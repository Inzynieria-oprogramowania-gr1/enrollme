import React, {useEffect, useState} from "react";
import {SpecifiedTimeSlot, Student, TimeSlot} from "../common/types";
import './Results.css';

const ENDPOINT = "http://localhost:8080/enrollment"

const Results = () => {
  const [linkStatus, setLinkStatus] = useState<string | null>(null);

  useEffect(() => {
    fetch(ENDPOINT + '/share')
      .then(response => response.json())
      .then(data => setLinkStatus(data.state))
      .catch(error => setLinkStatus('NOT_STARTED'));
  }, []);

  const [results, setResults] = useState([]);
  const [resultsMap, setResultsMap] = useState(new Map<SpecifiedTimeSlot, Student[]>());

  useEffect(() => {
    fetch(ENDPOINT + "/results")
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
  }, [setResults]);

  const renderResults = () => {
    console.log(linkStatus)
    if (linkStatus != 'RESULTS_READY' && linkStatus != 'CALCULATING') {
      return ( <p>Enrollment is not over yet. Please wait until it's closed to view the timetable.</p> );
    }
    return (
      <div>
        {
          Array.from(resultsMap.entries()).map(([timeslot, students], index) => (
            <div className="timeslot" key={index}>
              <p>Weekday: {timeslot.weekday}</p>
              <p>Start Date: {timeslot.start_date}</p>
              <p>End Date: {timeslot.end_date}</p>
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


  return (
    <div className="container">
      <h5>Results</h5>
      {renderResults()}
    </div>
  )
}

export default Results;