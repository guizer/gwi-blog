import axios from 'axios';
import { GetArticleArgs, PagedArticles } from './type';

export interface ArticleClient {
  getArticles: (args?: GetArticleArgs) => Promise<PagedArticles>;
}

const NAMESPACE = '/api/v1/articles';

export const createArticleClient = (): ArticleClient => {
  const httpClient = axios.create({
    baseURL: 'http://localhost:8080/',
  });
  return {
    getArticles: (args?: GetArticleArgs) =>
      httpClient
        .get<PagedArticles>(NAMESPACE, { params: { ...args } })
        .then((response) => response.data),
  };
};
