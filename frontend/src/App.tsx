import React, {useEffect, useState} from "react";
import TimeTable from "./teacher/TimeTable";
import Results from "./teacher/Results";
import "./App.css";




function App() {
  const handleButtonClick = () => {
    setShowResults(!showResults);
  }


  const [greeting, setGreeting] = useState('');
  const [showResults, setShowResults] = useState(false);
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
          <button onClick={handleButtonClick}>RESULTS</button>
      </header>
        {showResults ? <Results /> : <TimeTable />}
    </div>
  );
}

export default App;
