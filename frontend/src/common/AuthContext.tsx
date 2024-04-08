import React from 'react';

export const AuthContext = React.createContext({
  auth: '',
  setAuth: (auth: string) => {}
});