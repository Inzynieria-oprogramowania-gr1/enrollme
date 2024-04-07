import React, {useEffect, useState} from "react";
import {Route, Routes, useNavigate} from "react-router-dom";
import EnrollConfiguration from "./teacher/EnrollConfiguration";
import Results from "./teacher/Results";
import Login from "./common/Login";
import "./App.css";
import "bootstrap/dist/css/bootstrap.min.css";
import StudentTimeTable from "./student/StudentTimeTable";
import logo from "./resources/full_logo.png";
import {User} from "./common/types";

function App() {

  const navigate = useNavigate();
  const [user, setUser] = useState<User>({
    id: null,
    email: '',
    role: '',
    isAuthenticated: false
  })

  const handleResultsButtonClick = () => {
    navigate("/results")
  }
  const handleMakeTimeTableClick = () => {
    navigate("/timetable")
  }

  const handleLogin = (email: string, role: string) => {
    setUser({id: null, email: email, role: role, isAuthenticated: true})
  };

  return (
    <div className="App">
      <header className="App-header">
        <img className="full_logo" src={logo}></img>
        <button className="btn btn-primary" onClick={handleMakeTimeTableClick}>ENROLL CONFIG</button>
        <button className="btn btn-primary" onClick={handleResultsButtonClick}>RESULTS</button>
      </header>
      <Routes>
        <Route path="/students/timetable" element={user.isAuthenticated ? <StudentTimeTable user={user}/> :
          <Login onLogin={handleLogin} user={user} setUser={setUser} role="STUDENT"/>}/>

        <Route path="/results" element={user.isAuthenticated && user.role == 'TEACHER' ? <Results/> :
          <Login onLogin={handleLogin} user={user} setUser={setUser} role='TEACHER'/>}/>

        <Route path="/timetable" element={user.isAuthenticated && user.role == 'TEACHER' ? <EnrollConfiguration/> :
          <Login onLogin={handleLogin} user={user} setUser={setUser} role='TEACHER'/>}/>
      </Routes>
    </div>
  );
}

export default App;
