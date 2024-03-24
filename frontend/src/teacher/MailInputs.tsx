import React, {useState, FC} from "react";
import "./MailInputs.css";

interface MailInputsProps {
  linkStatus: string | null;
}

function isValidEmail(email: string) {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

const MailInputs: FC<MailInputsProps> = ({ linkStatus }) => {
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
    console.log(emails);
    const response = await fetch("http://localhost:8080/students", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
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
      <button className="btn btn-secondary" onClick={handleSendingMails} disabled={linkStatus == 'CALCULATING'}>Upload mails</button>
    </div>
  );
}

export default MailInputs;