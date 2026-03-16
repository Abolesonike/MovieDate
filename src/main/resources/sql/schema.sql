-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    nickname VARCHAR(50),
    avatar_url VARCHAR(500),
    bio TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建索引
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_created_at ON users(created_at);

-- 添加注释
COMMENT ON TABLE users IS '用户表';
COMMENT ON COLUMN users.id IS '用户 ID';
COMMENT ON COLUMN users.username IS '用户名';
COMMENT ON COLUMN users.email IS '邮箱';
COMMENT ON COLUMN users.password_hash IS '密码哈希';
COMMENT ON COLUMN users.nickname IS '昵称';
COMMENT ON COLUMN users.avatar_url IS '头像 URL';
COMMENT ON COLUMN users.bio IS '个人简介';
COMMENT ON COLUMN users.is_active IS '是否激活';
COMMENT ON COLUMN users.created_at IS '创建时间';
COMMENT ON COLUMN users.updated_at IS '更新时间';

-- 电影日记表（观影日历）
CREATE TABLE IF NOT EXISTS movie_diary (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    tmdb_movie_id INTEGER NOT NULL,
    movie_title VARCHAR(255) NOT NULL,
    watch_date DATE NOT NULL,
    rating DECIMAL(3,1) CHECK (rating >= 0 AND rating <= 10),
    review TEXT,
    poster_url VARCHAR(500),
    is_favorite BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_movie_diary_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 添加表和列注释
COMMENT ON TABLE movie_diary IS '电影日记表（观影日历）';
COMMENT ON COLUMN movie_diary.id IS '主键 ID';
COMMENT ON COLUMN movie_diary.user_id IS '用户 ID';
COMMENT ON COLUMN movie_diary.tmdb_movie_id IS 'TMDB 电影 ID';
COMMENT ON COLUMN movie_diary.movie_title IS '电影标题';
COMMENT ON COLUMN movie_diary.watch_date IS '观影日期';
COMMENT ON COLUMN movie_diary.rating IS '评分 (0-10)';
COMMENT ON COLUMN movie_diary.review IS '观影心得/评论';
COMMENT ON COLUMN movie_diary.poster_url IS '电影海报 URL';
COMMENT ON COLUMN movie_diary.is_favorite IS '是否收藏';
COMMENT ON COLUMN movie_diary.created_at IS '创建时间';
COMMENT ON COLUMN movie_diary.updated_at IS '更新时间';
