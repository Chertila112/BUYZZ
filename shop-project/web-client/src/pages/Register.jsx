import { useState } from "react";
import '../assets/style/Login.css';
import Login from "./Login";

function Register() {
  const [name, setName] = useState('');
  const [login, setLogin] = useState('');
  const [password, setPassword] = useState('');

  const handleRegister = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch('http://localhost:3000/api/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name, login, password })
      });

      const data = await response.json();
      alert(data.message);
    } catch (err) {
      console.error('Ошибка регистрации', err);
    }
  };

  return (
    <div className="login-container">
      <h1>Регистрация</h1>
      <form onSubmit={handleRegister} className="login-form">
        <label>
          Имя
          <input type="text" value={name} onChange={e => setName(e.target.value)} required />
        </label>
        <label>
          Email
          <input type="email" value={login} onChange={e => setLogin(e.target.value)} required />
        </label>
        <label>
          Пароль
          <input type="password" value={password} onChange={e => setPassword(e.target.value)} required />
        </label>
        <button type="submit">Зарегистрироваться</button>
      </form>
    </div>
  );
}

export default Register;
