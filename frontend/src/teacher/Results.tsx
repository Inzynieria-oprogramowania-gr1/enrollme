import React, {useEffect, useState} from "react";
import {SpecifiedTimeSlot, Student, TimeSlot} from "../common/types";
import './Results.css';

const Results = () => {
    const [results, setResults] = useState([]);
    const [resultsMap, setResultsMap] = useState(new Map<SpecifiedTimeSlot, Student[]>());

    useEffect(() => {
        fetch("http://localhost:8080/teacher/timetable/results")
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
    console.log(results)

    return (
        <div>
        <h2>Results</h2>
            {Array.from(resultsMap.entries()).map(([timeslot, students], index) => (
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
            ))}
        </div>
    );
}

export default Results;