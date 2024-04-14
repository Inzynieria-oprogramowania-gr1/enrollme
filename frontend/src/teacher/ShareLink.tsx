import React, {FC, useContext} from "react";
import {ShareLinkData} from "../common/types";
import {AuthContext} from "../common/AuthContext";
import {BASE_URL, FRONT_URL} from "../common/Constants";

const URL = BASE_URL + "/enrollment/share";

interface ShareLinkProps {
  linkStatus: string | null;
  setLinkStatus: (status: string) => void;
}

const ShareLink: FC<ShareLinkProps> = ({linkStatus, setLinkStatus}) => {
  const { auth } = useContext(AuthContext);

  const handleShareToStudents = async () => {
    try {
      let response = await fetch(URL, {
        headers: {
          'Authorization': auth
        }
      });
      if (!response.ok) {
        response = await fetch(URL, {
          method: "POST",
          headers: {
            'Authorization': auth
          }
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
      alert('The link is: ' + FRONT_URL + data.link);
      setLinkStatus('ACTIVE');
    } catch (error) {
      alert('Failed to save link to clipboard');
    }
  }

  return (
    <div>
      <button disabled={linkStatus === 'CALCULATING' || linkStatus === 'RESULTS_READY'} className="btn btn-secondary" onClick={handleShareToStudents}>Get link for students</button>
    </div>
  )
}

export default ShareLink;