import React, {useState, useEffect} from "react";
import "./MailInputs.css";

function isValidEmail(email: string) {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

const MailInputs = () => {
  const [textareaValue, setTextareaValue] = useState<string>('')

  const handleTextareaChange = (event: React.ChangeEvent<HTMLTextAreaElement>) => { // 3. Create an onChange handler
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
      <h5>Write list of the students mails, separate with semicolon:</h5>
        <textarea onChange={handleTextareaChange} />
      <button className="btn btn-secondary" onClick={handleSendingMails}>Send mails</button>
    </div>
  );
}

export default MailInputs;