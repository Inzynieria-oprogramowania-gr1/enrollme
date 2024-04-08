import React, {FC, useContext, useState} from "react";
import {User} from "../common/types";
import {AuthContext} from "../common/AuthContext";

interface LoginProps {
  onLogin: (email: string, role: string) => void;
  user: User;
  setUser: React.Dispatch<React.SetStateAction<User>>;
}


const TeacherLogin: FC<LoginProps> = ({onLogin, user, setUser}) => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const { setAuth } = useContext(AuthContext);

  const handleEmailChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setEmail(event.target.value);
  };

  const handlePasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setPassword(event.target.value);
  };


  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();

    const basicAuth = 'Basic ' + btoa(email + ':' + password);
    setAuth(basicAuth);

    fetch(`http://localhost:8080/auth/login`, {
      method: 'POST',
      headers: {
        'Authorization': basicAuth
      }
    })
      .then(response => {
        if (!response.ok) {
          throw new Error('The email or password is incorrect. Check it and try again');
        }
      })
      .then(data => {
        setUser({id: null, email: email, password: password, isAuthenticated: true});
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
        <label className="form-label">
          Password:
          <input className="form-control" type="password" value={password} onChange={handlePasswordChange} required/>
        </label>
        <button className="btn btn-secondary mt-3" type="submit">Submit</button>
      </form>
    </div>

  );
}

export default TeacherLogin;