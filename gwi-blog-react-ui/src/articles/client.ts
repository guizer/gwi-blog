import axios from 'axios';
import {
  GetArticlesArgs,
  GetArticlesByCategoryArgs,
  PagedArticles,
} from './type';

export interface ArticleClient {
  getArticles: (args?: GetArticlesArgs) => Promise<PagedArticles>;
  getArticlesByCategoryId: (
    args: GetArticlesByCategoryArgs,
  ) => Promise<PagedArticles>;
}

const NAMESPACE = '/api/v1/articles';

export const createArticleClient = (): ArticleClient => {
  const httpClient = axios.create({
    baseURL: 'http://localhost:8080/',
  });
  return {
    getArticles: (args?: GetArticlesArgs) => {
      const params = args
        ? { page: args.page?.toString(), pageSize: args.pageSize?.toString() }
        : {};
      return httpClient
        .get<PagedArticles>(NAMESPACE, {
          params,
        })
        .then((response) => response.data);
    },
    getArticlesByCategoryId: (args: GetArticlesByCategoryArgs) => {
      const params = args
        ? { page: args.page?.toString(), pageSize: args.pageSize?.toString() }
        : {};
      return httpClient
        .get<PagedArticles>(`${NAMESPACE}/category/${args?.categoryId}`, {
          params,
        })
        .then((response) => response.data);
    },
  };
};
