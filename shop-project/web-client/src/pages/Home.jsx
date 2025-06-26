import React, { useEffect, useState } from "react";
import ProductCard from "../components/ProductCard";
import axios from 'axios';

function Home() {
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(false);

    const fetchProducts = async () => {
        setLoading(true);
        try {
            const response = await axios.get('http://localhost:3000/api/products');
            setProducts(response.data);
        } catch(e) {
            console.error(e.description);
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        fetchProducts();
    }, []);

    if (loading) return <p style={{ padding: 20, color: "black" }}>Загрузка...</p>;

    return (
        <div className="products-container">
            {products.map((p) => (
                <ProductCard key={p.id} product={p} />
            ))}
        </div>
    );
}

export default Home;