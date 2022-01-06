import { FC, useEffect, useState } from 'react';
import { ArticlesDisplay, useArticleClient } from './articles';
import { PagedArticles } from './articles/type';

const Home: FC = () => {
  const [articles, setArticles] = useState<PagedArticles>();
  const articlesClient = useArticleClient();
  useEffect(() => {
    if (articlesClient) {
      articlesClient.getArticles().then(setArticles);
    }
  }, [articlesClient]);
  return articles ? <ArticlesDisplay articles={articles?.articles} /> : null;
};

export default Home;
