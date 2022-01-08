import { FC, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import { useCategories, useCategoryClient } from '../context';
import CategoriesDisplay from './CategoriesDisplay';

const CategoriesNav: FC = () => {
  const [categories, setCategories] = useCategories();
  const categoryClient = useCategoryClient();
  const navigate = useNavigate();
  useEffect(() => {
    categoryClient
      .getCategories()
      .then((fetchedCategories) =>
        setCategories(
          fetchedCategories.reduce(
            (obj, category) => ({
              ...obj,
              [category.slug]: category,
            }),
            {},
          ),
        ),
      )
      // eslint-disable-next-line no-console
      .catch((error) => console.log(error));
  }, [categoryClient, setCategories]);
  const onCategorySelected = (categorySlug: string) =>
    navigate(`/category/${categorySlug}`);
  return (
    <CategoriesDisplay
      categories={Object.values(categories)}
      onCategorySelected={onCategorySelected}
    />
  );
};

export default CategoriesNav;
