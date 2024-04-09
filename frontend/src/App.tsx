import React, {useState} from "react";
import {Route, Routes, useNavigate} from "react-router-dom";
import EnrollConfiguration from "./teacher/EnrollConfiguration";
import Results from "./teacher/Results";
import StudentLogin from "./student/StudentLogin";
import "./App.css";
import "bootstrap/dist/css/bootstrap.min.css";
import StudentTimeTable from "./student/StudentTimeTable";
import logo from "./resources/full_logo.png";
import {User} from "./common/types";
import TeacherLogin from "./teacher/TeacherLogin";
import { AuthContext } from "./common/AuthContext";

function App() {
  const [auth, setAuth] = useState('');

  const navigate = useNavigate();
  const [user, setUser] = useState<User>({
    id: null,
    email: '',
    password: null,
    isAuthenticated: false
  })

  const handleResultsButtonClick = () => {
    navigate("/results")
  }
  const handleMakeTimeTableClick = () => {
    navigate("/timetable")
  }

  const handleStudentLogin = (email: string) => {
    setUser({id: null, email: email, password: null, isAuthenticated: true})
  };

  const handleTeacherLogin = (email: string, password: string) => {
    setUser({id: null, email: email, password: password, isAuthenticated: true})
  };

  return (
    <div className="App">
      <header className="App-header">
        <img className="full_logo" src={logo} alt="Logo"></img>
        <button className="btn btn-primary" onClick={handleMakeTimeTableClick}>ENROLL CONFIG</button>
        <button className="btn btn-primary" onClick={handleResultsButtonClick}>RESULTS</button>
      </header>
      <AuthContext.Provider value={{ auth, setAuth }}>
        <Routes>

          <Route path="/students/timetable" element={user.isAuthenticated ? <StudentTimeTable user={user}/> :
            <StudentLogin onLogin={handleStudentLogin} user={user} setUser={setUser}/>}/>

          <Route path="/results" element={user.isAuthenticated && user.password != null ? <Results/> :
            <TeacherLogin onLogin={handleTeacherLogin} user={user} setUser={setUser}/>}/>

          <Route path="/timetable" element={user.isAuthenticated && user.password != null ? <EnrollConfiguration/> :
            <TeacherLogin onLogin={handleTeacherLogin} user={user} setUser={setUser} />}/>
        </Routes>
      </AuthContext.Provider>

    </div>
  );
}

export default App;
