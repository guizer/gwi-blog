import { FC } from 'react';
import { Outlet } from 'react-router-dom';
import Container from 'react-bootstrap/esm/Container';
import './App.css';
import Header from './Header';
import Footer from './Footer';
import { CategoriesNav, CategoryClientContextProvider } from './categories';

const App: FC = () => (
  <>
    <Header />
    <div className="body">
      <Container>
        <CategoryClientContextProvider>
          <CategoriesNav />
        </CategoryClientContextProvider>
        <Outlet />
      </Container>
    </div>

    <Footer />
  </>
);

export default App;
