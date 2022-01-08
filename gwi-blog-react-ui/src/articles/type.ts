import { Category } from '../categories';

interface Tag {
  id: string;
  name: string;
}

export interface Article {
  id: number;
  imageUrl: string;
  title: string;
  content: string;
  publishedAt: string;
  lastModifiedAt: string;
  category: Category;
  tags: Tag[];
  author: string;
}

export interface PagedArticles {
  articles: Article[];
  totalPages: number;
  totalElements: number;
}

export type GetArticleArgs = {
  page?: string;
  pageSize?: string;
};
