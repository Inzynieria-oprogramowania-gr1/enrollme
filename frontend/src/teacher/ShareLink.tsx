import React from "react";
import {ShareLinkData} from "../types";

const handleShareToStudents = async () => {
  try {
    let response = await fetch("http://localhost:8080/teacher/timetable/share");
    if (!response.ok) {
      response = await fetch("http://localhost:8080/teacher/timetable/share", {
        method: "POST",
      });
      if (!response.ok) {
        alert('Failed to create share link');
        return;
      }
    }
    const data: ShareLinkData = await response.json();
    if (data.state === 'INACTIVE') {
      alert('The link is currently inactive');
      return;
    }
    await navigator.clipboard.writeText("http://localhost:3000" + data.link);
    alert('Link has been saved to clipboard');
  } catch (error) {
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