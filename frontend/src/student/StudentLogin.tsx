import React, {useState, FC} from 'react';
import {useNavigate} from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import './StudentLogin.css';

interface StudentLoginProps {
  onLogin: () => void;
}

const StudentLogin: FC<StudentLoginProps> = ({onLogin}) => {
  const [email, setEmail] = useState('');
  const history = useNavigate();

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    // TO-DO request to the backend to verify email
    // if (response.ok) {
    //   history('/students/timetable');
    // } else {
    //   alert('Invalid email. Please try again.');
    // }
    onLogin();
    history('/students/timetable');

  };

  return (
    <div className="login-container">
      <form onSubmit={handleSubmit} className="login-form">
        <label className="form-label">
          Enter your email:
          <input className="form-control" type="email" value={email} onChange={e => setEmail(e.target.value)} required/>
        </label>
        <button className="btn btn-secondary mt-3" type="submit">Submit</button>
      </form>
    </div>

  );
};

export default StudentLogin;