-- BOOKS
CREATE TABLE books (
  id BIGSERIAL PRIMARY KEY,
  title VARCHAR(200) NOT NULL,
  author VARCHAR(120) NOT NULL,
  isbn VARCHAR(20) NOT NULL,
  total_copies INTEGER NOT NULL,
  available_copies INTEGER NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  CONSTRAINT uq_books_isbn UNIQUE (isbn),
  CONSTRAINT ck_books_copies CHECK (
    total_copies >= 0 AND available_copies >= 0 AND available_copies <= total_copies
  )
);

CREATE INDEX idx_books_title ON books(title);
CREATE INDEX idx_books_author ON books(author);

-- MEMBERS
CREATE TABLE members (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(120) NOT NULL,
  email VARCHAR(254) NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  CONSTRAINT uq_members_email UNIQUE (email)
);

-- LOANS
CREATE TABLE loans (
  id BIGSERIAL PRIMARY KEY,
  book_id BIGINT NOT NULL REFERENCES books(id),
  member_id BIGINT NOT NULL REFERENCES members(id),
  borrowed_at TIMESTAMPTZ NOT NULL,
  due_date TIMESTAMPTZ NOT NULL,
  returned_at TIMESTAMPTZ
);

CREATE INDEX idx_loans_member_active
  ON loans(member_id)
  WHERE returned_at IS NULL;

CREATE INDEX idx_loans_member_due
  ON loans(member_id, due_date)
  WHERE returned_at IS NULL;

CREATE INDEX idx_loans_book_active
  ON loans(book_id)
  WHERE returned_at IS NULL;