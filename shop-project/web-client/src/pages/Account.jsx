import React, { useEffect, useState } from 'react';
import axios from 'axios';
import '../assets/style/Account.css';

const Account = () => {
  // Инициализируем user из localStorage, если есть
  const savedUser = JSON.parse(localStorage.getItem('user')) || { name: '', login: '' };
  const [user, setUser] = useState(savedUser);
  const [orders, setOrders] = useState([]);
  const [message, setMessage] = useState('');

  const token = localStorage.getItem('token');

  useEffect(() => {
    if (token) {
      fetchUser();
      fetchOrders();
    }
  }, []);

  const fetchUser = async () => {
    try {
      const res = await axios.get('http://localhost:3000/api/users/me', {
        headers: { Authorization: `Bearer ${token}` },
      });
      setUser(res.data);
      localStorage.setItem('user', JSON.stringify(res.data));
    } catch (err) {
      console.error('Ошибка загрузки профиля:', err);
    }
  };

  const fetchOrders = async () => {
    try {
      const res = await axios.get('http://localhost:3000/api/orders', {
        headers: { Authorization: `Bearer ${token}` },
      });
      setOrders(res.data);
    } catch (err) {
      console.error('Ошибка загрузки заказов:', err);
    }
  };

  return (
    <div className="account-container">
      {message && <p className="message">{message}</p>}

      <div className="info-section">
        <h2 className="greeting">Привет, {user.name}!</h2>
        <p>Ваш email: <strong>{user.login}</strong></p>
      </div>

      <h2>Мои заказы</h2>
      {orders.length === 0 ? (
        <p>Нет заказов</p>
      ) : (
        <ul className="orders-list">
          {orders.map((order) => (
            <li key={order.id}>
              Заказ #{order.id}, статус: {order.status}, дата:{' '}
              {new Date(order.created_at).toLocaleString()}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default Account;
