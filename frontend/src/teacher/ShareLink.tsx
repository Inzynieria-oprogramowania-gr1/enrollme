import React, {FC} from "react";
import {RELEASE_ENDPOINT, RELEASE_FRONT_ENDPOINT, ShareLinkData} from "../common/types";

const ENDPOINT = RELEASE_ENDPOINT + "/enrollment/share";

const FRONT_ENDPOINT = RELEASE_FRONT_ENDPOINT;

interface ShareLinkProps {
  linkStatus: string | null;
  setLinkStatus: (status: string) => void;
}

const ShareLink: FC<ShareLinkProps> = ({linkStatus, setLinkStatus}) => {

  const handleShareToStudents = async () => {
    try {
      let response = await fetch(ENDPOINT);
      if (!response.ok) {
        response = await fetch(ENDPOINT, {
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
      // await navigator.clipboard.writeText(FRONT_ENDPOINT + data.link);
      alert('Link has been saved to clipboard. The link is: ' + FRONT_ENDPOINT + data.link);
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