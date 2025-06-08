import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import '../assets/style/Login.css';

function Login({ onLogin }) {
  const [login, setLogin] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    try {
      // Логинимся
      const { data } = await axios.post('http://localhost:3000/auth/login', { login, password });

      localStorage.setItem('token', data.token);
      localStorage.setItem('user', JSON.stringify(data.user));

      if (onLogin) {
        onLogin(data.user);
      }

      navigate('/');
    } catch (err) {
      if (err.response) {
        setError(err.response.data.error || 'Ошибка входа');
      } else {
        setError('Сервер недоступен');
      }
      console.error(err);
    }
  };

  return (
    <div className="login-container">
      <h1>Вход в аккаунт</h1>
      <form onSubmit={handleSubmit} className="login-form">
        {error && <p className="error">{error}</p>}

        <label>
          Электронная почта
          <input 
            type="email" 
            value={login} 
            onChange={e => setLogin(e.target.value)} 
            required 
            placeholder="example@mail.com"
          />
        </label>

        <label>
          Пароль
          <input 
            type="password" 
            value={password} 
            onChange={e => setPassword(e.target.value)} 
            required 
            placeholder="Введите пароль"
          />
        </label>

        <button type="submit">Войти</button>
      </form>
    </div>
  );
}

export default Login;
