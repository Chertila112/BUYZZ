import "../assets/style/Navbar.css";
import { Link } from 'react-router-dom';

function NavBar() {
  return (
    <nav className="navbar">
      <Link to="/">
        <img src="/logo1.png" alt="Buyzz Logo" className="site-logo" />
      </Link>
      <div className="nav-links">
        <Link to="/cart">Корзина</Link>
        <Link to="/login">Вход</Link>
      </div>
    </nav>
  );
}

export default NavBar;