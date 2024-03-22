import React from "react";
import "./TimeTable.css";
import MailInputs from "./MailInputs";
import 'bootstrap/dist/css/bootstrap.min.css';
import ShareLink from "./ShareLink";
import TimeTable from "./TimeTable";

const EnrollConfiguration = () => {
  return (
    <div className="container">
      <h5 className="mb-3">Choose preferred time slots:</h5>
      <TimeTable />
      <div className="d-flex justify-content-between align-items-center">
        <MailInputs></MailInputs>
        <ShareLink></ShareLink>
      </div>
    </div>
  );
};
export default EnrollConfiguration;