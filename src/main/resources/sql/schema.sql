-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY COMMENT '用户 ID',
    username VARCHAR(50) UNIQUE NOT NULL COMMENT '用户名',
    email VARCHAR(100) UNIQUE NOT NULL COMMENT '邮箱',
    password_hash VARCHAR(255) NOT NULL COMMENT '密码哈希',
    nickname VARCHAR(50) COMMENT '昵称',
    avatar_url VARCHAR(500) COMMENT '头像 URL',
    bio TEXT COMMENT '个人简介',
    is_active BOOLEAN DEFAULT TRUE COMMENT '是否激活',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间'
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
    id BIGSERIAL PRIMARY KEY COMMENT '主键 ID',
    user_id BIGINT NOT NULL COMMENT '用户 ID',
    tmdb_movie_id INTEGER NOT NULL COMMENT 'TMDB 电影 ID',
    movie_title VARCHAR(255) NOT NULL COMMENT '电影标题',
    watch_date DATE NOT NULL COMMENT '观影日期',
    rating DECIMAL(3,1) CHECK (rating >= 0 AND rating <= 10) COMMENT '评分 (0-10)',
    review TEXT COMMENT '观影心得/评论',
    poster_url VARCHAR(500) COMMENT '电影海报 URL',
    is_favorite BOOLEAN DEFAULT FALSE COMMENT '是否收藏',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    CONSTRAINT fk_movie_diary_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 创建索引
CREATE INDEX idx_movie_diary_user_id ON movie_diary(user_id);
CREATE INDEX idx_movie_diary_tmdb_movie_id ON movie_diary(tmdb_movie_id);
CREATE INDEX idx_movie_diary_watch_date ON movie_diary(watch_date);
CREATE INDEX idx_movie_diary_created_at ON movie_diary(created_at);

-- 添加注释
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
