import React, {useState, FC} from 'react';
import {useNavigate} from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import './Login.css';
import {User} from "./types";

interface LoginProps {
  onLogin: (email: string, role: string) => void;
  user: User;
  setUser: React.Dispatch<React.SetStateAction<User>>;
  role: string;
}

const Login: FC<LoginProps> = ({onLogin, user, setUser, role}) => {
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
          throw new Error('The provided mail was not found. Check it and try again');
        }
        return response.json();
      })
      .then(data => {
        switch (role) {
          case 'STUDENT':
            setUser({ id: data.id, email: email, role: data.role, isAuthenticated: true });
            break;
          case 'TEACHER':
            if (data.role == 'TEACHER') {
              setUser({ id: data.id, email: email, role: data.role, isAuthenticated: true });
            } else {
              throw new Error('You can not log in to teacher only page with the role of student');
            }
            break;
        }
      })
      .catch((error: Error) => {
        alert(error.message);
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

export default Login;