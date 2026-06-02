-- R2DBC schema: applied on startup when spring.sql.init.mode=always (datastore profile).
-- Uses IF NOT EXISTS so it is safe to re-run.

CREATE TABLE IF NOT EXISTS articles (
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    content     TEXT,
    author      VARCHAR(128),
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

-- Index for common lookup patterns
CREATE INDEX IF NOT EXISTS idx_articles_author    ON articles (author);
CREATE INDEX IF NOT EXISTS idx_articles_created   ON articles (created_at DESC);
