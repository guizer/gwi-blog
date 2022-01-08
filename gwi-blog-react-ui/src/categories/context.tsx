import React, { FC, useContext, useMemo, useState } from 'react';
import { CategoryClient, createCategoryClient } from './client';
import { Category, CategoryByName } from './type';

type CategoryClientContextState = {
  categoryClient?: CategoryClient;
  categories?: [
    CategoryByName,
    React.Dispatch<React.SetStateAction<CategoryByName>>,
  ];
  selectedCategory?: Category;
};

export const CategoryClientContext =
  React.createContext<CategoryClientContextState>({});

export const CategoryClientContextProvider: FC = ({ children }) => {
  const categories = useState<CategoryByName>({});
  const [categoryClient] = useState<CategoryClient>(createCategoryClient());
  const state = useMemo(
    () => ({
      categories,
      categoryClient,
    }),
    [categories, categoryClient],
  );
  return (
    <CategoryClientContext.Provider value={state}>
      {children}
    </CategoryClientContext.Provider>
  );
};

export const useCategoryClient = () => {
  const { categoryClient } = useContext(CategoryClientContext);
  if (categoryClient) {
    return categoryClient;
  }
  throw Error('useCategoryClient must be used within a CategoryClientContext');
};

export const useCategories = () => {
  const { categories } = useContext(CategoryClientContext);
  if (categories) {
    return categories;
  }
  throw Error('useCategories must be used within a CategoryClientContext');
};

export const useSelectedCategory = () => {
  const { selectedCategory } = useContext(CategoryClientContext);
  if (selectedCategory) {
    return selectedCategory;
  }
  throw Error(
    'useSelectedCategory must be used within a CategoryClientContext',
  );
};
