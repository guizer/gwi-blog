import { FC, useEffect, useMemo, useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import { ArticlesDisplay, PagedArticles, useArticleClient } from './articles';
import Pagination from './pagination';

const ARTICLES_PER_PAGE = 5;

const Home: FC = () => {
  const [pagedArticles, setPagedArticles] = useState<PagedArticles>();
  const articlesClient = useArticleClient();
  const [searchParams, setSearchParams] = useSearchParams();
  const pageFromUrl = Number(searchParams.get('page') || '1');
  const [currentPage, setCurrentPage] = useState(pageFromUrl);

  useEffect(() => {
    if (articlesClient) {
      articlesClient
        .getArticles({
          page: currentPage.toString(),
          pageSize: ARTICLES_PER_PAGE.toString(),
        })
        .then(setPagedArticles)
        // eslint-disable-next-line no-console
        .catch((error) => console.log(error));
    }
  }, [articlesClient, currentPage]);

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

export default Home;
