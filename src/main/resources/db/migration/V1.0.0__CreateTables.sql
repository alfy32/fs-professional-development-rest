CREATE TABLE todo (
  todo_id SERIAL PRIMARY KEY,
  created TIMESTAMP DEFAULT now(),
  todo TEXT NOT NULL,
  done TIMESTAMP
);