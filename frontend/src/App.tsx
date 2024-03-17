import React, {useEffect, useState} from "react";
import {Route, Routes, useNavigate} from "react-router-dom";
import TimeTable from "./teacher/TimeTable";
import Results from "./teacher/Results";
import StudentLogin from "./student/StudentLogin";
import "./App.css";
import "bootstrap/dist/css/bootstrap.min.css";
import StudentTimeTable from "./student/StudentTimeTable";

function App() {

  const navigate = useNavigate();
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  const handleResultsButtonClick = () => {
    navigate("/results")
  }
  const handleMakeTimeTableClick = () => {
    navigate("/timetable")
  }

  const handleLogin = () => {
    setIsAuthenticated(true);
  };

  const [greeting, setGreeting] = useState('');
  const [showResults, setShowResults] = useState(false);
  const [showTimeTable, setShowTimeTable] = useState(false);
  useEffect(() => {
    fetch("http://localhost:8080/demo/hi")
      .then(res => res.text())
      .then(setGreeting)
      .catch(console.error);
  }, [setGreeting]);
  return (
    <div className="App">
      <header className="App-header">
        {greeting ? (
          <p>{greeting}</p>
        ) : (
          <p>Loading...</p>
        )}
          <button className="btn btn-primary" onClick={handleMakeTimeTableClick}>MAKE TIMETABLE</button>
          <button className="btn btn-primary" onClick={handleResultsButtonClick}>RESULTS</button>
      </header>
      <Routes>
        <Route path="/students/timetable" element={isAuthenticated ? <StudentTimeTable/> : <StudentLogin onLogin={handleLogin}/>} />
        <Route path="/results" element={<Results />} />
        <Route path="/timetable" element={<TimeTable />} />
      </Routes>
    </div>
  );
}

export default App;
