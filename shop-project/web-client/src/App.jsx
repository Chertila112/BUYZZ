import { BrowserRouter, Routes, Route } from "react-router-dom"
import Home from "./pages/Home"
import Cart from "./pages/Cart"
import Product from "./pages/Product"
import Login from "./pages/Login"
import Navbar from "./components/Navbar"
import Register from "./pages/Register"
import './App.css'
import { useState } from "react"
import { useEffect } from "react"

function App() {
  const [user, setUser] = useState(null);

  useEffect(() => {
    const savedUser = localStorage.getItem("user");
    if(savedUser) {
      setUser(JSON.parse(savedUser));
    }
  }, []);


  return (
    <BrowserRouter>
      <Navbar user={user} setUser={setUser}/>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/cart" element={<Cart />} />
        <Route path="/login" element={<Login onLogin={setUser}/>} />
        <Route path="/product/:id" element={<Product />} />
        <Route path="/register" element={<Register />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App
