import { useEffect, useState } from "react";
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import '../assets/style/Cart.css';

function Cart() {
  const [cartItems, setCartItems] = useState([]);
  const [error, setError] = useState(null);
  const [deliveryAddress, setDeliveryAddress] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    const loadCart = async () => {
      const storedUser = JSON.parse(localStorage.getItem("user"));
      const token = localStorage.getItem("token");
      const userId = storedUser?.id;

      if (!userId || !token) {
        setError("Пользователь не авторизован");
        return;
      }

      try {
        const res = await axios.get(`http://localhost:3000/api/cart`, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });

        setCartItems(res.data);
      } catch (e) {
        console.error('Error during loading cart', e);
        setError('Ошибка при загрузке корзины')
      }
    };

    loadCart();
  }, []);

  const updateQuantity = async (id, delta) => {
    const token = localStorage.getItem("token");

    try {
      await axios.patch(`http://localhost:3000/api/cart/items/${id}`, { delta }, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });

      setCartItems(cartItems.map(item =>
        item.id === id ? { ...item, quantity: item.quantity + delta } : item
      ));
    } catch (e) {
      console.error('Ошибка при изменении количества:', e);
    }
  };

  const removeItem = async (id) => {
    const storedUser = JSON.parse(localStorage.getItem("user"));
    const token = localStorage.getItem("token");

    try {
      await axios.delete(`http://localhost:3000/api/cart/remove/${id}`, {
        headers: {
          'Authorization': `Bearer ${token}`
        },
      });

      setCartItems(cartItems.filter(item => item.id !== id));

    } catch (e) {
      console.error('Error during removing item', e);
    }
  };

  const totalPrice = cartItems.reduce((total, item) => total + item.price * item.quantity, 0);

  const handleCheckout = async () => {
    const token = localStorage.getItem("token");

    if (!deliveryAddress.trim()) {
      alert("Введите адрес доставки");
      return;
    }

    try {
      await axios.post("http://localhost:3000/api/orders", {
        delivery_address: deliveryAddress
      }, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });

      alert("Заказ оформлен успешно!");
      navigate('/account');
    } catch (e) {
      console.error("Error during making purchase: ", e);
      alert("Ошибка при оформлении заказа");
    }
  };

  return (
    <div className="cart-container">
      <h1>Корзина</h1>
      {error ? (
        <p className="cart-empty">{error}</p>
      ) : cartItems.length === 0 ? (
        <p className="cart-empty">Ваша корзина пуста</p>
      ) : (
        <>
          <ul className="cart-list">
            {cartItems.map(item => (
              <li key={item.id} className="cart-item">
                <div className="cart-info">
                  <strong>{item.name}</strong>
                  <span>Цена: {item.price} ₽ x {item.quantity} шт.</span>
                </div>

                <div className="cart-actions-group">
                  <button
                    onClick={() => updateQuantity(item.id, -1)}
                    disabled={item.quantity <= 1}
                    className="qty-button"
                  >-</button>

                  <span className="qty">{item.quantity}</span>

                  <button
                    onClick={() => updateQuantity(item.id, 1)}
                    className="qty-button"
                  >+</button>

                  <button
                    onClick={() => removeItem(item.id)}
                    className="remove-button"
                  >Удалить</button>
                </div>
              </li>
            ))}
          </ul>

          <h2 className="cart-total">Итого: {totalPrice.toFixed(2)} ₽</h2>
          <input
            type="text"
            placeholder="Введите адрес доставки"
            value={deliveryAddress}
            onChange={(e) => setDeliveryAddress(e.target.value)}
            className="delivery-input"
          />
          <button className="checkout-button" onClick={handleCheckout}>Оформить заказ</button>
        </>
      )}
    </div>
  );
}

export default Cart;
