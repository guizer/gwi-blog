import { FC } from 'react';
import Col from 'react-bootstrap/esm/Col';
import Row from 'react-bootstrap/esm/Row';
import { Article } from '../type';

interface ArticleDisplayProps {
  article: Article;
}

const ArticleDisplay: FC<ArticleDisplayProps> = ({ article }) => (
  <article>
    <Row>
      <Col xs={12} sm={6} md={4}>
        <div className="post-image">
          <img src={article.imageUrl} alt={article.title} />
        </div>
      </Col>
      <Col xs={12} sm={6} md={8}>
        <div className="post-content">
          <h2>{article.title}</h2>
          <span className="publication-date">
            Published at {new Date(article.publishedAt).toLocaleString()}
          </span>
          <div>{article.content}</div>
        </div>
      </Col>
    </Row>
  </article>
);

export default ArticleDisplay;
