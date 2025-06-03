import { useState } from "react";
import '../assets/style/Cart.css';

function Cart() {
  const [cartItems, setCartItems] = useState([
    { id: 1, name: "Product1", price: 800, quantity: 2 },
    { id: 2, name: "Product2", price: 700, quantity: 1 },
  ]);

  const removeItem = (id) => {
    setCartItems(cartItems.filter(item => item.id !== id));
  };

  const totalPrice = cartItems.reduce((total, item) => total + item.price * item.quantity, 0);

  return (
    <div className="cart-container">
      <h1>Корзина</h1>
      
      {cartItems.length === 0 ? (
        <p className="cart-empty">Ваша корзина пуста.</p>
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
