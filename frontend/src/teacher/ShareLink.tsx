import React from "react";

const handleShareToStudents = async () => {
  try {
    const response = await fetch("http://localhost:8080/teacher/timetable/share", {
      method: "POST",
    });
    if (!response.ok) {
      alert('Failed to fetch share link');
      return;
    }

    const data = await response.json();
    await navigator.clipboard.writeText("http://localhost:3000" + data.link);
    alert('Link has been saved to clipboard');
  } catch (error) {
    console.error('Error:', error);
    alert('Failed to save link to clipboard');
  }
}

const ShareLink = () => {
  return (
    <div>
      <button className="btn btn-secondary" onClick={handleShareToStudents}>Get link for students</button>
    </div>
  )
}

export default ShareLink;