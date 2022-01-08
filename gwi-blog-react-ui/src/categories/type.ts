export interface Category {
  id: number;
  name: string;
  slug: string;
}

export type CategoryBySlug = { [categorySlug: string]: Category };
