import { Link } from 'react-router-dom';
import "../assets/style/ProductCard.css"

function ProductCard({ product }) {
    return (
        <Link to={`/product/${product.id}`}>
            <div className="product-card">
                <img src={`/product${product.id}.jpg`} alt={product.name}/>
                <h3>{product.name}</h3>
                <p className='price'>{product.price}₽</p>
            </div>
        </Link>
    );
}

export default ProductCard;