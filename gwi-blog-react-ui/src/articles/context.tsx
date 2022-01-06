import React, { FC, useContext, useMemo } from 'react';
import { ArticleClient, createArticleClient } from './client';

type ArticleClientContextState = {
  articleClient?: ArticleClient;
};

export const ArticleClientContext =
  React.createContext<ArticleClientContextState>({});

export const ArticleClientContextProvider: FC = ({ children }) => {
  const state = useMemo(() => ({ articleClient: createArticleClient() }), []);
  return (
    <ArticleClientContext.Provider value={state}>
      {children}
    </ArticleClientContext.Provider>
  );
};

export const useArticleClient = () => {
  const { articleClient } = useContext(ArticleClientContext);
  return articleClient;
};
