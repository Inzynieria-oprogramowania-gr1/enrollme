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
      <h5 className="mb-3">Choose preferred time slots:</h5>
      <TimeTable linkStatus={linkStatus} setLinkStatus={setLinkStatus}/>
      <div className="d-flex justify-content-between align-items-center">
        <MailInputs linkStatus={linkStatus}></MailInputs>
        <ShareLink linkStatus={linkStatus} setLinkStatus={setLinkStatus}></ShareLink>
      </div>
    </div>
  );
};
export default EnrollConfiguration;