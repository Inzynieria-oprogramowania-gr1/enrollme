import React, {useEffect, useState} from "react";

const Results = () => {
    const [results, setResults] = useState([]);
    useEffect(() => {
        fetch("http://localhost:8080/teacher/timetable/results")
        .then(res => res.json())
        .then(setResults)
        .catch(console.error);
    }, [setResults]);

    console.log(results)
    return (
        <div>
        <h2>Results</h2>
        </div>
    );
}

export default Results;