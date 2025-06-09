require('dotenv').config();
const express = require('express');
const { Pool } = require('pg');
const cors = require('cors');
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');

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

// ==================== JWT auth middleware ==================
function authenticateToken(req, res, next) {
  const authHeader = req.headers['authorization'];
  const token = authHeader && authHeader.split(' ')[1]; // "Bearer <token>"

  if (!token) return res.status(401).json({ error: 'Нет токена' });

  jwt.verify(token, process.env.JWT_SECRET || 'complex_secret_key_at_least_32_chars_long', (err, user) => {
    if (err) return res.status(403).json({ error: 'Неверный токен' });

    req.userId = user.userId;
    next();
  });
}

pool
  .connect()
  .then(() => console.log('Подключено к PostgreSQL'))
  .catch((err) => console.error('Ошибка подключения', err.stack));

// ======================= Users =======================

app.get('/api/users', async (req, res) => {
  try {
    const { rows } = await pool.query('SELECT * FROM users');
    res.json(rows);
  } catch (err) {
    console.error(err);
    res.status(500).send('Ошибка сервера');
  }
});

app.get('/api/users/me', authenticateToken, async (req, res) => {
  try {
    const userId = req.userId;
    const { rows } = await pool.query('SELECT id, name, login FROM users WHERE id = $1', [userId]);

    if (rows.length === 0) {
      return res.status(404).json({ error: 'Пользователь не найден' });
    }

    res.json(rows[0]);
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: 'Ошибка получении профиля' });
  }
});

// ======================= Auth =======================

app.post('/auth/login', async (req, res) => {
  const { login, password } = req.body;

  try {
    const result = await pool.query('SELECT * FROM users WHERE login = $1', [login]);

    if (result.rows.length === 0) {
      return res.status(401).json({ error: 'Пользователь не найден' });
    }

    const user = result.rows[0];
    const isValidPassword = await bcrypt.compare(password, user.password_hash);

    if (!isValidPassword) {
      return res.status(401).json({ error: 'Неверный пароль' });
    }

    const token = jwt.sign({ userId: user.id }, process.env.JWT_SECRET || 'complex_secret_key_at_least_32_chars_long', {
      expiresIn: '1h',
    });

    res.json({
      token,
      user: {
        id: user.id,
        name: user.name,
        login: user.login,
      },
    });
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: 'Ошибка сервера' });
  }
});

app.post('/auth/register', async (req, res) => {
  const { name, login, password } = req.body;

  try {
    const existing = await pool.query('SELECT * FROM users WHERE login = $1', [login]);
    if (existing.rows.length > 0) {
      return res.status(400).json({ error: 'Такой логин уже существует' });
    }

    const passwordHash = await bcrypt.hash(password, 10);
    const result = await pool.query(
      'INSERT INTO users (name, login, password_hash) VALUES ($1, $2, $3) RETURNING id, name, login',
      [name, login, passwordHash]
    );

    const user = result.rows[0];
    const token = jwt.sign({ userId: user.id }, process.env.JWT_SECRET || 'complex_secret_key_at_least_32_chars_long', {
      expiresIn: '1h',
    });

    res.json({ token, user });
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: 'Ошибка регистрации' });
  }
});

// ======================= Products =======================

app.get('/api/products', async (req, res) => {
  try {
    const { rows } = await pool.query('SELECT * FROM products');
    res.json(rows);
  } catch (err) {
    res.status(500).json({ error: 'Ошибка при получении товаров' });
  }
});

app.post('/api/products', async (req, res) => {
  const { name, price, description, stock_quantity } = req.body;

  try {
    await pool.query(
      'INSERT INTO products (name, price, description, stock_quantity) VALUES ($1, $2, $3, $4)',
      [name, price, description, stock_quantity]
    );
    res.status(201).send('Товар добавлен');
  } catch (err) {
    res.status(500).json({ error: 'Ошибка при добавлении товара' });
  }
});

// ======================= Cart =======================

app.get('/api/cart', authenticateToken, async (req, res) => {
  const userId = req.userId;

  try {
    const { rows } = await pool.query(
      `SELECT ci.*, p.name, p.price 
       FROM cart_items ci
       JOIN products p ON ci.product_id = p.id
       WHERE ci.cart_id = (SELECT id FROM carts WHERE user_id = $1)`,
      [userId]
    );
    res.json(rows);
  } catch (error) {
    res.status(500).json({ error: 'Ошибка получения корзины' });
  }
});

app.post('/api/cart/items', authenticateToken, async (req, res) => {
  const userId = req.userId;
  const { product_id, quantity } = req.body;

  try {
    const user = await pool.query('SELECT id FROM users WHERE id = $1', [userId]);
    if (user.rows.length === 0) {
      return res.status(400).json({ error: 'Пользователь не найден' });
    }

    let cartResult = await pool.query('SELECT id FROM carts WHERE user_id = $1', [userId]);
    let cartId;

    if (cartResult.rows.length === 0) {
      const newCart = await pool.query(
        'INSERT INTO carts (user_id) VALUES ($1) RETURNING id',
        [userId]
      );
      cartId = newCart.rows[0].id;
    } else {
      cartId = cartResult.rows[0].id;
    }

    const existing = await pool.query(
      'SELECT id, quantity FROM cart_items WHERE cart_id = $1 AND product_id = $2',
      [cartId, product_id]
    );
    
    if (existing.rows.length > 0) {
      await pool.query(
        'UPDATE cart_items SET quantity = quantity + $1 WHERE id = $2',
        [quantity, existing.rows[0].id]
      );
    } else {
      await pool.query(
        'INSERT INTO cart_items (cart_id, product_id, quantity) VALUES ($1, $2, $3)',
        [cartId, product_id, quantity]
      );
    }

    res.status(201).json({ message: 'Добавлено в корзину', cartId });
  } catch (error) {
    console.error('Ошибка при добавлении в корзину:', error);
    res.status(500).json({ error: 'Ошибка при добавлении в корзину' });
  }
});

app.get('/api/cart/items', authenticateToken, async (req, res) => {
  const userId = req.userId;

  try {
    const { rows } = await pool.query(`
      SELECT 
        ci.id,
        ci.quantity,
        p.id as product_id,
        p.name,
        p.price,
        p.description
      FROM cart_items ci
      JOIN products p ON ci.product_id = p.id
      WHERE ci.cart_id = (SELECT id FROM carts WHERE user_id = $1)
    `, [userId]);

    res.json(rows);
  } catch (err) {
    console.error('Ошибка получения корзины:', err);
    res.status(500).json({ error: 'Ошибка сервера' });
  }
});

app.patch('/api/cart/items/:itemId', authenticateToken, async (req, res) => {
  const userId = req.userId;
  const { itemId } = req.params;
  const { delta } = req.body;

  try {
    const { rows } = await pool.query(
      `SELECT ci.id, ci.quantity 
       FROM cart_items ci
       JOIN carts c ON ci.cart_id = c.id
       WHERE ci.id = $1 AND c.user_id = $2`,
      [itemId, userId]
    );

    if (rows.length === 0) {
      return res.status(404).json({ error: 'Элемент не найден или доступ запрещён' });
    }

    const currentQty = rows[0].quantity;
    const newQty = currentQty + delta;

    if (newQty < 1) {
      return res.status(400).json({ error: 'Количество не может быть меньше 1' });
    }

    await pool.query(
      'UPDATE cart_items SET quantity = $1 WHERE id = $2',
      [newQty, itemId]
    );

    res.status(200).json({ message: 'Количество обновлено', quantity: newQty });
  } catch (err) {
    console.error('Ошибка при обновлении количества:', err);
    res.status(500).json({ error: 'Ошибка сервера' });
  }
});


app.delete('/api/cart/remove/:itemId', authenticateToken, async (req, res) => {
  const userId = req.userId;
  const itemId = req.params.itemId;

  try {
    const { rows } = await pool.query(
      `SELECT ci.id FROM cart_items ci
       JOIN carts c ON ci.cart_id = c.id
       WHERE ci.id = $1 AND c.user_id = $2`,
      [itemId, userId]
    );

    if (rows.length === 0) {
      return res.status(404).json({ error: 'Элемент не найден или доступ запрещён' });
    }

    await pool.query('DELETE FROM cart_items WHERE id = $1', [itemId]);

    res.status(200).json({ message: 'Элемент удалён' });
  } catch (err) {
    console.error('Ошибка при удалении из корзины:', err);
    res.status(500).json({ error: 'Ошибка сервера при удалении элемента' });
  }
});

// ======================= Orders =======================

app.get('/api/orders', authenticateToken, async (req, res) => {
  const userId = req.userId;

  try {
    const { rows } = await pool.query(
      `SELECT o.id, o.delivery_address, o.status, o.created_at 
       FROM orders o
       WHERE o.user_id = $1`,
      [userId]
    );
    res.json(rows);
  } catch (err) {
    console.error(err);
    res.status(500).send('Ошибка при получении заказов');
  }
});

app.get('/api/orders/:orderId/items', authenticateToken, async (req, res) => {
  const { orderId } = req.params;

  try {
    const { rows } = await pool.query(
      `SELECT * FROM order_items WHERE order_id = $1`,
      [orderId]
    );
    res.json(rows);
  } catch (err) {
    console.error(err);
    res.status(500).send('Ошибка запроса позиций заказа');
  }
});

app.get('/api/orders/:orderId/history', authenticateToken, async (req, res) => {
  const { orderId } = req.params;

  try {
    const { rows } = await pool.query(
      `SELECT * FROM order_status_history WHERE order_id = $1`,
      [orderId]
    );
    res.json(rows);
  } catch (err) {
    console.error(err);
    res.status(500).send('Ошибка запроса истории заказа');
  }
});

app.post('/api/orders', authenticateToken, async (req, res) => {
  const userId = req.userId;
  const { delivery_address } = req.body;

  try {
      const result = await pool.query(
  `INSERT INTO orders (user_id, delivery_address, status)
   VALUES ($1, $2, $3) RETURNING *`,
  [userId, delivery_address, 'pending']
  );
    res.status(201).json(result.rows[0]);
  } catch (err) {
    console.error(err);
    res.status(500).send('Ошибка создания заказа');
  }
});


const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`Сервер запущен на порту ${PORT}`);
});