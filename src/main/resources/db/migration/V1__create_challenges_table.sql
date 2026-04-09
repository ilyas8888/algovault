CREATE TABLE challenges (
    id         BIGSERIAL PRIMARY KEY,
    title      VARCHAR(255) NOT NULL,
    difficulty VARCHAR(50)  NOT NULL,
    category   VARCHAR(100) NOT NULL
);

INSERT INTO challenges (title, difficulty, category) VALUES
    ('Two Sum',              'EASY',   'Arrays'),
    ('Reverse a String',     'EASY',   'Strings'),
    ('Binary Search',        'MEDIUM', 'Arrays'),
    ('Valid Parentheses',    'MEDIUM', 'Stacks'),
    ('Merge K Sorted Lists', 'HARD',   'Linked Lists');
