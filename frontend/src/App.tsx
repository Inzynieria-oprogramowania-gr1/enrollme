import React, {useEffect, useState} from "react";
import TimeTable from "./teacher/TimeTable";
import "./App.css";


function App() {
  const [greeting, setGreeting] = useState('');
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
      </header>
      <TimeTable />
    </div>
  );
}

export default App;
