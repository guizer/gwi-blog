import axios from 'axios';
import { Category } from './type';

export interface CategoryClient {
  getCategories: () => Promise<Category[]>;
}

const NAMESPACE = '/api/v1/categories';

export const createCategoryClient = (): CategoryClient => {
  const httpClient = axios.create({
    baseURL: 'http://localhost:8080/',
  });
  return {
    getCategories: () =>
      httpClient.get<Category[]>(NAMESPACE).then((response) => response.data),
  };
};
