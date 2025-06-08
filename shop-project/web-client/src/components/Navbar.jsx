import "../assets/style/Navbar.css";
import { Link, useNavigate } from 'react-router-dom';


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
      <Link to="/">
        <img src="/logo1.png" alt="Buyzz Logo" className="site-logo" />
      </Link>
      <div className="nav-links">
        
        {user? (
          <>
            <span>Привет, {user.name}</span>
            <Link to="/cart">Корзина</Link>
            <button onClick={hadleLogout} className="link-style">Выйти</button>
          </>
        ) : (
          <>
            <Link to="/login">Вход</Link>
            <Link to="/register">Регистрация</Link>
          </>
        )}
      </div>
    </nav>
  );
}

export default NavBar;