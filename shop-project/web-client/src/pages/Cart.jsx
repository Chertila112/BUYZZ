  import { useEffect, useState } from "react";
  import axios from 'axios';
  import '../assets/style/Cart.css';

  function Cart() {
    const [cartItems, setCartItems] = useState([]);
    const [error, setError] = useState(null);
    
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
        const res = await axios.patch(`http://localhost:3000/api/cart/items/${id}`, { delta }, {
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
      const userId = storedUser?.id;

      try {
        await axios.delete(`http://localhost:3000/api/cart/remove/${id}`, {
          headers: {
            'Authorization': `Bearer ${token}`
          },
        });
        
        setCartItems(cartItems.filter(item => item.id != id));

      } catch (e) {
        console.error('Error during removing item', e);
      }
    };

    const totalPrice = cartItems.reduce((total, item) => total + item.price * item.quantity, 0);

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
                  <div>
                    <strong>{item.name}</strong><br />
                    <span>Цена: {item.price} ₽ x {item.quantity} шт.</span>
                  </div>
                  <div className="cart-actions">
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
                  </div>
                  <button 
                    onClick={() => removeItem(item.id)} 
                    className="remove-button"
                  >Удалить</button>
                </li>
              ))}
            </ul>

            <h2 className="cart-total">Итого: {totalPrice.toFixed(2)} ₽</h2>
            <button className="checkout-button">Оформить заказ</button>
          </>
        )}
      </div>
    );
  }

  export default Cart;
