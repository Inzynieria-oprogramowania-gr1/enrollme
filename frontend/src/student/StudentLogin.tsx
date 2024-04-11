import React, {useState, FC} from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import './StudentLogin.css';
import {User} from "../common/types";
import {BASE_URL} from "../common/Constants";

interface LoginProps {
  onLogin: (email: string, role: string) => void;
  user: User;
  setUser: React.Dispatch<React.SetStateAction<User>>;
}

const StudentLogin: FC<LoginProps> = ({onLogin, user, setUser}) => {
  const [email, setEmail] = useState('');

  const handleEmailChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setEmail(event.target.value);
  };

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();

    fetch(BASE_URL + `/students?email=${email}`)
      .then(response => {
        if (!response.ok) {
          throw new Error('The provided mail was not found. Check it and try again');
        }
        return response.json();
      })
      .then(data => {
        fetch(BASE_URL + '/enrollment/share')
          .then(response => response.json())
          .then(shareData => {
            if (shareData.state !== 'ACTIVE') {
              alert('Enrollment has not started or has already ended');
              return;
            }
            setUser({id: data.id, email: email, password: null, isAuthenticated: true});
          });
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

export default StudentLogin;