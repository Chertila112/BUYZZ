import { useParams } from "react-router-dom";
import { useEffect, useState } from "react"; 
import "../assets/style/Product.css"; 
import axios from 'axios';

function Product() {
    const { id } = useParams();
    const [product, setProduct] = useState();
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        const fetchProductById = async () => {
            setLoading(true);
            try {
                const response = await axios.get('http://localhost:3000/api/products');
                const found = response.data.find(p => p.id == id);
                setProduct(found);
            } catch (e) {
                console.error(e.message);
            } finally {
                setLoading(false);
            }
        };

        fetchProductById();
    }, [id]);

    if (loading) return <p style={{ padding: 20, color: "black" }}>Загрузка...</p>;
    if (!product) return <p style={{ padding: 20 }}>Товар не найден</p>;

    return (
        !loading&&product&&
        <div className="product-page">
            <h2>{product.name}</h2>
            <img src={`/product${product.id}.jpg`} alt={product.name}/>
            <p>{product.description}</p>
            <p className="price">Цена: {product.price}₽</p>
            <button>Добавить в корзину</button>
        </div>
    );
}

export default Product;
