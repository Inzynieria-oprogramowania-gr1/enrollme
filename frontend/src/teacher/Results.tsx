import React, {useEffect, useState} from "react";

const Results = () => {
    const [results, setResults] = useState([]);
    useEffect(() => {
        fetch("http://localhost:8080/teacher/results")
        .then(res => res.json())
        .then(setResults)
        .catch(console.error);
    }, [setResults]);
    return (
        <div>
        <h2>Results</h2>
        <ul>
            {results.map((result, index) => (
            <li key={index}>{result}</li>
            ))}
        </ul>
        </div>
    );
}

export default Results;