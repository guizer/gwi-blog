import { FC } from 'react';
import { Article } from '../type';
import ArticleDisplay from './ArticleDisplay';

interface ArticlesDisplayProps {
  articles: Article[];
}

const ArticlesDisplay: FC<ArticlesDisplayProps> = ({ articles }) => (
  <div className="articles">
    {articles.map((article) => (
      <ArticleDisplay key={article.id} article={article} />
    ))}
  </div>
);

export default ArticlesDisplay;
