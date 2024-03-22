import React, {useState, FC} from 'react';
import {useNavigate} from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import './StudentLogin.css';
import {User} from "../types";

interface StudentLoginProps {
  onLogin: (email: string) => void;
  user: User;
  setUser: React.Dispatch<React.SetStateAction<User>>;
}

const StudentLogin: FC<StudentLoginProps> = ({onLogin, user, setUser}) => {
  const [email, setEmail] = useState('');
  const history = useNavigate();

  const handleEmailChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setEmail(event.target.value);
  };

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();

    fetch(`http://localhost:8080/students?email=${email}`)
      .then(response => {
        if (!response.ok) {
          throw new Error('Failed to check email');
        }
        return response.json();
      })
      .then(data => {
        setUser({ id: data.id, email: email, isAuthenticated: true });
        history('/students/timetable');
      })
      .catch((error) => {
        console.error('Error:', error);
        window.alert('Failed to log in. Please check your email and try again.');
      });
  };

  return (
    <div className="login-container">
      <form onSubmit={handleSubmit} className="login-form">
        <label className="form-label">
          Enter your email:
          <input className="form-control" type="email" value={email} onChange={handleEmailChange} required/>
        </label>
        <button className="btn btn-secondary mt-3" type="submit">Submit</button>
      </form>
    </div>

  );
};

export default StudentLogin;