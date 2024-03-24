import React, {FC} from "react";
import {ShareLinkData} from "../common/types";

interface ShareLinkProps {
  linkStatus: string | null;
  setLinkStatus: (status: string) => void;
}

const ShareLink: FC<ShareLinkProps> = ({linkStatus, setLinkStatus}) => {

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
      if (data.state === 'CALCULATING') {
        alert('The link is currently inactive');
        return;
      }
      await navigator.clipboard.writeText("http://localhost:3000" + data.link);
      alert('Link has been saved to clipboard');
      setLinkStatus('ACTIVE');
    } catch (error) {
      alert('Failed to save link to clipboard');
    }
  }

  return (
    <div>
      <button disabled={linkStatus == 'CALCULATING' || linkStatus === 'RESULTS_READY'} className="btn btn-secondary" onClick={handleShareToStudents}>Get link for students</button>
    </div>
  )
}

export default ShareLink;