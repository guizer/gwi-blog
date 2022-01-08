import { FC, useEffect, useMemo, useState } from 'react';
import { useParams, useSearchParams } from 'react-router-dom';
import { ArticlesDisplay, PagedArticles, useArticleClient } from '../articles';
import { useCategories } from '../categories/context';
import Pagination from '../pagination';
import { ARTICLES_PER_PAGE, DEFAULT_PAGE } from './config';

const ArticlesByCategory: FC = () => {
  const [pagedArticles, setPagedArticles] = useState<PagedArticles>();
  const articlesClient = useArticleClient();
  const [searchParams, setSearchParams] = useSearchParams();
  const { categoryName } = useParams<{ categoryName: string }>();
  const [categories] = useCategories();
  const [currentPage, setCurrentPage] = useState(
    Number(searchParams.get('page') || DEFAULT_PAGE),
  );

  useEffect(() => {
    if (categoryName && categories[categoryName]) {
      articlesClient
        .getArticlesByCategoryId({
          categoryId: categories[categoryName].id,
          page: currentPage,
          pageSize: ARTICLES_PER_PAGE,
        })
        .then(setPagedArticles)
        // eslint-disable-next-line no-console
        .catch((error) => console.log(error));
    }
  }, [articlesClient, categories, categoryName, currentPage]);

  const paginationProps = useMemo(
    () => ({
      currentPage,
      onClick: (pageClicked: number) => {
        setSearchParams({ page: pageClicked.toString() });
        setCurrentPage(pageClicked);
      },
      totalPages: pagedArticles ? pagedArticles.totalPages : 1,
    }),
    [pagedArticles],
  );

  return pagedArticles ? (
    <>
      <Pagination {...paginationProps} />
      <ArticlesDisplay articles={pagedArticles.articles} />
      <Pagination {...paginationProps} />
    </>
  ) : null;
};

export default ArticlesByCategory;
