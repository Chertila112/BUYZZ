import { useState } from "react";
import '../assets/style/Login.css';

function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    // Здесь можно добавить логику аутентификации
    alert(`Вход для: ${email}`);
  };

  return (
    <div className="login-container">
      <h1>Вход в аккаунт</h1>
      <form onSubmit={handleSubmit} className="login-form">
        <label>
          Электронная почта
          <input 
            type="email" 
            value={email} 
            onChange={e => setEmail(e.target.value)} 
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