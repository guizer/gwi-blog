import { FC } from 'react';
import { Link } from 'react-router-dom';
import Container from 'react-bootstrap/esm/Container';
import Navbar from 'react-bootstrap/esm/Navbar';
import logo from './logo.svg';

const Header: FC = () => (
  <header>
    <Navbar bg="dark" variant="dark">
      <Container>
        <Link to="/">
          <Navbar.Brand>
            <img
              alt=""
              src={logo}
              width="30"
              height="30"
              className="d-inline-block align-top"
            />
            GWI Blog
          </Navbar.Brand>
        </Link>
      </Container>
    </Navbar>
  </header>
);

export default Header;
