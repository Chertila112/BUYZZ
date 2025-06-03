import React, { useEffect, useState } from "react";
import ProductCard from "../components/ProductCard";
import img1 from '../assets/img/product1.jpg';
import img2 from '../assets/img/product2.jpg';
import img3 from '../assets/img/product3.jpg';

function Home() {
    const [products, setProducts] = useState([]);

    useEffect(() => {
        setProducts([
            {id: 1, name: "Product1", price: "800", image: img1},
            {id: 2, name: "Product2", price: "700", image: img2},
            {id: 3, name: "Product3", price: "600", image: img3},
        ]);
    }, []);

    return (
        <div className="products-container">
            {products.map((p) => (
                <ProductCard key={p.id} product={p} />
            ))}
        </div>
    );
}

export default Home;