import React, {useState, FC, useContext} from "react";
import "./MailInputs.css";
import {AuthContext} from "../common/AuthContext";
import {BASE_URL} from "../common/Constants";

interface MailInputsProps {
  linkStatus: string | null;
}

function isValidEmail(email: string) {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

const MailInputs: FC<MailInputsProps> = ({ linkStatus }) => {
  const { auth } = useContext(AuthContext);
  const [textareaValue, setTextareaValue] = useState<string>('')

  const handleTextareaChange = (event: React.ChangeEvent<HTMLTextAreaElement>) => {
    setTextareaValue(event.target.value);
  }

  const handleSendingMails = async () => {
    const emails = textareaValue.replace(/\s+/g, '').split(';');
    for (let email of emails) {
      if (!isValidEmail(email)) {
        alert(`Invalid email: ${email}`);
        return;
      }
    }
    const response = await fetch(BASE_URL + "/students", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        'Authorization': auth
      },
      body: JSON.stringify(emails)
    });
    if (response.ok) {
      alert("Emails sent successfully");
      return;
    }
    alert(`Error sending emails: ${response.statusText}`);
  }

  return (
    <div className="mail-div">
      <h6>Write list of the students mails, separate with semicolon:</h6>
        <textarea onChange={handleTextareaChange} />
      <button className="btn btn-secondary" onClick={handleSendingMails} disabled={linkStatus === 'CALCULATING' || linkStatus === 'RESULTS_READY'}>Upload mails</button>
    </div>
  );
}

export default MailInputs;