import { FC } from 'react';
import BootstrapPagination from 'react-bootstrap/esm/Pagination';

const { First, Item, Last, Next, Prev } = BootstrapPagination;

interface PaginationProps {
  currentPage: number;
  totalPages: number;
  onClick?: (page: number) => void;
}

const Pagination: FC<PaginationProps> = ({
  currentPage,
  onClick,
  totalPages,
}) => (
  <BootstrapPagination>
    <First
      onClick={onClick ? () => onClick(1) : undefined}
      disabled={currentPage === 1}
    />
    <Prev
      onClick={onClick ? () => onClick(currentPage - 1) : undefined}
      disabled={currentPage === 1}
    />
    {Array.from({ length: totalPages }, (v, k) => k)
      .filter(
        (value) => value + 1 >= currentPage - 3 && value + 1 <= currentPage + 3,
      )
      .map((value) => (
        <Item
          key={value + 1}
          active={value + 1 === currentPage}
          onClick={onClick ? () => onClick(value + 1) : undefined}
        >
          {value + 1}
        </Item>
      ))}
    <Next
      onClick={onClick ? () => onClick(currentPage + 1) : undefined}
      disabled={currentPage === totalPages}
    />
    <Last
      onClick={onClick ? () => onClick(totalPages) : undefined}
      disabled={currentPage === totalPages}
    />
  </BootstrapPagination>
);

export default Pagination;
