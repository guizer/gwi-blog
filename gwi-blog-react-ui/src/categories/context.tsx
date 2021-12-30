import React, { FC, useContext, useMemo } from 'react';
import { CategoryClient, createCategoryClient } from './client';

type CategoryClientContextState = {
  categoryClient?: CategoryClient;
};

export const CategoryClientContext =
  React.createContext<CategoryClientContextState>({});

export const CategoryClientContextProvider: FC = ({ children }) => {
  const state = useMemo(() => ({ categoryClient: createCategoryClient() }), []);
  return (
    <CategoryClientContext.Provider value={state}>
      {children}
    </CategoryClientContext.Provider>
  );
};

export const useCategoryClient = () => {
  const { categoryClient } = useContext(CategoryClientContext);
  return categoryClient;
};
