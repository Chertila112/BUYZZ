import { useState } from "react";
import { useNavigate } from "react-router-dom"; 
import axios from "axios";
import '../assets/style/Login.css';

function Register() {
  const [name, setName] = useState('');
  const [login, setLogin] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleRegister = async (e) => {
    e.preventDefault();

    try {
      const { data } = await axios.post('http://localhost:3000/auth/register', { name, login, password });

      localStorage.setItem('token', data.token);
      localStorage.setItem('user', JSON.stringify(data.user));

      navigate('/');
    } catch (err) {
      if (err.response) {
        alert(err.response.data.error || 'Ошибка регистрации');
      } else {
        alert('Произошла ошибка во время регистрации');
      }
      console.error('Error during registration', err);
    }
  };

  return (
    <div className="login-container">
      <h1>Регистрация</h1>
      <form onSubmit={handleRegister} className="login-form">
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
          <input type="password" placeholder="Введите пароль" value={password} onChange={e => setPassword(e.target.value)} required />
        </label>
        <button type="submit">Зарегистрироваться</button>
      </form>
    </div>
  );
}

export default Register;
