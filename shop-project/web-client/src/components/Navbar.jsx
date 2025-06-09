import "../assets/style/Navbar.css";
import { NavLink, useNavigate } from 'react-router-dom';

function NavBar({ user, setUser }) {
  const navigate = useNavigate();

  const hadleLogout = () => {
    setUser(null);
    localStorage.removeItem("user");
    localStorage.removeItem("token");
    navigate("/");
  };

  return (
    <nav className="navbar">
      <NavLink to="/">
        <img src="/logo1.png" alt="Buyzz Logo" className="site-logo" />
      </NavLink>
      <div className="nav-links">
        {user ? (
          <>
            <NavLink to="/cart" className={({ isActive }) => isActive ? "active-link" : undefined}>
              Корзина
            </NavLink>
            <NavLink to="/account" className={({ isActive }) => isActive ? "active-link" : undefined}>
              Личный кабинет
            </NavLink>
            <button onClick={hadleLogout} className="link-style">Выйти</button>
          </>
        ) : (
          <>
            <NavLink to="/login" className={({ isActive }) => isActive ? "active-link" : undefined}>
              Вход
            </NavLink>
            <NavLink to="/register" className={({ isActive }) => isActive ? "active-link" : undefined}>
              Регистрация
            </NavLink>
          </>
        )}
      </div>
    </nav>
  );
}

export default NavBar;
