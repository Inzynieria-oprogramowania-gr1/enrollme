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
  const [user, setUser] = useState<{ id: null | number, email: string, isAuthenticated: boolean }>({
    id: null,
    email: '',
    isAuthenticated: false
  });

  const handleResultsButtonClick = () => {
    navigate("/results")
  }
  const handleMakeTimeTableClick = () => {
    navigate("/timetable")
  }

  const handleLogin = (email: string) => {
    setUser({id: null, email: email, isAuthenticated: true})
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

  console.log(user)
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
        <Route path="/students/timetable" element={user.isAuthenticated ? <StudentTimeTable user={user}/> :
          <StudentLogin onLogin={handleLogin} user={user} setUser={setUser}/>}/>
        <Route path="/results" element={<Results/>}/>
        <Route path="/timetable" element={<TimeTable/>}/>
      </Routes>
    </div>
  );
}

export default App;
