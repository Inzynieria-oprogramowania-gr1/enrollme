import React, {useState, useEffect} from "react";


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
  const [timeTableData, setTimeTableData] = useState<Day[]>([])
  useEffect(() => {
    fetch("http://localhost:8080/teacher/timetable")
      .then(response => response.json())
      // .then()
      .then(setTimeTableData)
      .catch(err => console.error(err))
  }, [setTimeTableData])

  // console.log(timeTableData)
  return (
    <div>
      <h1>Time Table</h1>
      <table>
        <thead>
        <tr>
          <th>Day</th>
          <th>Timeslots</th>
        </tr>
        </thead>
        <tbody>
        {timeTableData.map((day, index) => (
          <tr key={index}>
            <td>{day.weekday}</td>
            <td>
              {day.timeSlots.map((slot, i) => (
                <div key={i}>
                  {slot.start_date} - {slot.end_date}
                </div>
              ))}
            </td>
          </tr>
        ))}
        </tbody>
      </table>
    </div>
  );
}

export default TimeTable;