import { useParams } from "react-router-dom";
import { useEffect, useState } from "react"; 
import img1 from '../assets/img/product1.jpg';
import img2 from '../assets/img/product2.jpg';
import img3 from '../assets/img/product3.jpg';
import "../assets/style/Product.css"; 

function Product() {
    const { id } = useParams();
    const [product, setProduct] = useState(null);

    useEffect(() => {
        const products = [
            { id: 1, name: "Product1", price: "800", image: img1, description: "Описание товара 1" },
            { id: 2, name: "Product2", price: "700", image: img2, description: "Описание товара 2" },
            { id: 3, name: "Product3", price: "600", image: img3, description: "Описание товара 3" },
        ];

        const found = products.find(p => p.id === parseInt(id));
        setProduct(found);
    }, [id]);

    if (!product) return <p style={{ padding: 20 }}>Товар не найден</p>;

    return (
        <div className="product-page">
            <h2>{product.name}</h2>
            <img src={product.image} alt={product.name} />
            <p>{product.description}</p>
            <p className="price">Цена: {product.price}₽</p>
            <button>Добавить в корзину</button>
        </div>
    );
}

export default Product;
