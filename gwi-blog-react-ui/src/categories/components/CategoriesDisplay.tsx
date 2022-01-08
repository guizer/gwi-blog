import { FC } from 'react';
import Nav from 'react-bootstrap/esm/Nav';
import { Category } from '../type';

interface CategoriesDisplayProps {
  categories: Category[];
  activeCategory?: string;
  onCategorySelected?: (categorySlug: string) => void;
}

const CategoriesDisplay: FC<CategoriesDisplayProps> = ({
  activeCategory,
  categories,
  onCategorySelected,
}) => (
  <Nav
    activeKey={activeCategory}
    className="categories-nav"
    onSelect={(key) => {
      if (onCategorySelected && key !== null) {
        onCategorySelected(key);
      }
    }}
    variant="pills"
  >
    {categories
      .sort((categoryA, categoryB) =>
        categoryA.name.localeCompare(categoryB.name),
      )
      .map((category) => (
        <Nav.Item key={category.slug}>
          <Nav.Link eventKey={category.slug}>{category.name}</Nav.Link>
        </Nav.Item>
      ))}
  </Nav>
);

export default CategoriesDisplay;
