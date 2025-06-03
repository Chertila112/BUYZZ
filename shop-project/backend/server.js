require('dotenv').config();
const express = require('express');
const { Pool } = require('pg');
const cors = require('cors');
const { use } = require('react');


const app = express();
app.use(cors());
app.use(express.json());

const pool = new Pool({
  user: process.env.DB_USER || 'admin',
  host: process.env.DB_HOST || 'db',
  database: process.env.DB_NAME || 'shop',
  password: process.env.DB_PASSWORD || 'admin123',
  port: process.env.DB_PORT || 5432,
});

const corsOptions = {
  origin: "*",
  optionsSuccessStatus: 200
}

app.use(cors(corsOptions));
pool.connect()
  .then(() => console.log('Connected to PostgreSQL'))
  .catch(err => console.error('Connection error', err.stack));

app.get('/api/users', async (req, res) => {
  try {
    const { rows } = await pool.query('SELECT * FROM users');
    res.json(rows);
  } catch (err) {
    console.error(err);
    res.status(500).send('Server error');
  }
});

app.get('api/orders/:user_id', async (req, res) => {
  try {
    const {userId} = req.params;
    const {rows} = await pool.query(`SELECT (SELECT name FROM users WHERE id = $1), o.id, o.delivery_address, o.status, o.created_at 
      FROM orders o
      WHERE o.id = (SELECT id FROM orders WHERE user_id = $1);`, [userId]);
    res.json(rows);
  }
  catch(err) {
    console.error(err);
    res.status(500).send('Server error');
  }
});

app.get('/api/products', async (req, res) => {
  const { rows } = await pool.query('SELECT * FROM products');
  res.json(rows);
});

app.post('/api/products', async (req, res) => {
  const { name, price, description, stock_quantity } = req.body;
  await pool.query(
    'INSERT INTO products (name, price, description, stock_quantity) VALUES ($1, $2, $3, $4)',
    [name, price, description, stock_quantity]
  );
  res.status(201).send('Product added');
});

app.get('/api/cart/:userId', async (req, res) => {
  const { userId } = req.params;
  const { rows } = await pool.query(
    `SELECT ci.*, p.name, p.price 
     FROM cart_items ci
     JOIN products p ON ci.product_id = p.id
     WHERE ci.cart_id = (SELECT id FROM carts WHERE user_id = $1)`,
    [userId]
  );
  res.json(rows);
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});