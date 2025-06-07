import { useEffect, useState } from "react";
import '../assets/style/Cart.css';

function Cart() {
  const [cartItems, setCartItems] = useState([]);
  const [error, setError] = useState(null);
  
  useEffect(() => {
    const storedUser = JSON.parse(localStorage.getItem("user"));
    const userId = storedUser?.id;

    if (!userId) {
      setError("Пользователь не авторизован");
      return;
    }

    fetch(`http://localhost:3000/api/cart/${userId}`)
      .then(res => res.json())
      .then(data => setCartItems(data))
      .catch((e) => {
        console.error('Error during loading cart', e);
        setError('Ошибка при загрузке корзины')
      });
  }, []);

  const removeItem = (id) => {
    setCartItems(cartItems.filter(item => item.id !== id));
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
                <button 
                  onClick={() => removeItem(item.id)} 
                  className="remove-button"
                >
                  Удалить
                </button>
              </li>
            ))}
          </ul>

          <h2 className="cart-total">Итого: {totalPrice} ₽</h2>
          <button className="checkout-button">Оформить заказ</button>
        </>
      )}
    </div>
  );
}

export default Cart;
