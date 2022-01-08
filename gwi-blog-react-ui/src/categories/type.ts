export interface Category {
  id: number;
  name: string;
}

export type CategoryByName = { [categoryName: string]: Category };
