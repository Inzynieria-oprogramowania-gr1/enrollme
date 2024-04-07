import React, {useState, useEffect} from "react";
import "./TimeTable.css";
import MailInputs from "./MailInputs";
import 'bootstrap/dist/css/bootstrap.min.css';
import ShareLink from "./ShareLink";
import TimeTable from "./TimeTable";

const EnrollConfiguration = () => {
  const [linkStatus, setLinkStatus] = useState<string | null>(null);

  useEffect(() => {
    fetch('http://localhost:8080/enrollment/share')
      .then(response => response.json())
      .then(data => setLinkStatus(data.state))
      .catch(error => setLinkStatus('NOT_STARTED'));
  }, []);

  return (
    <div className="container">
      <TimeTable linkStatus={linkStatus} setLinkStatus={setLinkStatus}/>
      <h5 className="mb-2 mt-5">Access for students:</h5>
      <div className="d-flex justify-content-between align-items-center">
        <MailInputs linkStatus={linkStatus}></MailInputs>
        <ShareLink linkStatus={linkStatus} setLinkStatus={setLinkStatus}></ShareLink>
      </div>
    </div>
  );
};
export default EnrollConfiguration;