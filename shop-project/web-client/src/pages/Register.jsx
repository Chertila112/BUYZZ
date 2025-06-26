import { useState } from "react";
import { useNavigate } from "react-router-dom"; 
import axios from "axios";
import '../assets/style/Login.css';

function Register() {
  const [name, setName] = useState('');
  const [login, setLogin] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleRegister = async (e) => {
    e.preventDefault();
    setError('');

    // Validate password confirmation
    if (password !== confirmPassword) {
      setError('Пароли не совпадают');
      return;
    }

    // Validate password length
    if (password.length < 6) {
      setError('Пароль должен содержать минимум 6 символов');
      return;
    }

    try {
      const { data } = await axios.post('http://localhost:3000/auth/register', { name, login, password });

      localStorage.setItem('token', data.token);
      localStorage.setItem('user', JSON.stringify(data.user));

      navigate('/');
    } catch (err) {
      if (err.response) {
        setError(err.response.data.error || 'Ошибка регистрации');
      } else {
        setError('Произошла ошибка во время регистрации');
      }
      console.error('Error during registration', err);
    }
  };

  return (
    <div className="login-container">
      <h1>Регистрация</h1>
      <form onSubmit={handleRegister} className="login-form">
        {error && <p className="error">{error}</p>}
        
        <label>
          Имя
          <input type="text" placeholder="Имя" value={name} onChange={e => setName(e.target.value)} required />
        </label>
        <label>
          Email
          <input type="email" placeholder="example@mail.com" value={login} onChange={e => setLogin(e.target.value)} required />
        </label>
        <label>
          Пароль
          <input 
            type="password" 
            placeholder="Введите пароль" 
            value={password} 
            onChange={e => setPassword(e.target.value)} 
            required 
            minLength={6}
          />
        </label>
        <label>
          Подтвердите пароль
          <input 
            type="password" 
            placeholder="Повторите пароль" 
            value={confirmPassword} 
            onChange={e => setConfirmPassword(e.target.value)} 
            required 
            minLength={6}
          />
        </label>
        <button type="submit">Зарегистрироваться</button>
      </form>
    </div>
  );
}

export default Register;
