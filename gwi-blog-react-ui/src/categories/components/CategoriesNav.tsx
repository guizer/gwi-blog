import { FC, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import { useCategoryClient } from '../context';
import { Category } from '../type';
import CategoriesDisplay from './CategoriesDisplay';

const CategoriesNav: FC = () => {
  const [categories, setCategories] = useState<Category[]>([]);
  const categoryClient = useCategoryClient();
  const navigate = useNavigate();
  useEffect(() => {
    if (categoryClient) {
      categoryClient
        .getCategories()
        .then(setCategories)
        // eslint-disable-next-line no-console
        .catch((error) => console.log(error));
    }
  }, [categoryClient]);
  const onCategorySelected = (categoryName: string) =>
    navigate(`/categories/${categoryName}`);

  return (
    <CategoriesDisplay
      categories={categories}
      onCategorySelected={onCategorySelected}
    />
  );
};

export default CategoriesNav;
