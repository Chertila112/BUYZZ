import { BrowserRouter, Routes, Route } from "react-router-dom"
import Home from "./pages/Home"
import Cart from "./pages/Cart"
import Catalog from "./pages/Home"
import Product from "./pages/Product"
import Login from "./pages/Login"
import Navbar from "./components/Navbar"
import './App.css'

function App() {
  return (
    <BrowserRouter>
      <Navbar />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/cart" element={<Cart />} />
        <Route path="/login" element={<Login />} />
        <Route path="/product/:id" element={<Product />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App
