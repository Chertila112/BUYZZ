import { Link } from 'react-router-dom';
import "../assets/style/ProductCard.css"

function ProductCard({ product }) {
    return (
        <Link to={`/product/${product.id}`} style={{ textDecoration: 'none', color: 'inherit' }}>
            <div className="product-card" style={{ border: "1px solid #ccc", padding: 10, margin: 10, width: 200 }}>
                <img src={product.image} alt={product.name} style={{ width: "100%" }} />
                <h3>{product.name}</h3>
                <p>{product.price}₽</p>
            </div>
        </Link>
    );
}

export default ProductCard;