import { Navbar, Container, Image, Button, Row, Col } from "react-bootstrap";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { logout, selectIsAuthenticated } from "../../../store/slices/authSlice";
import { selectCurrentUser } from "../../../store/slices/authSlice";
import { useGetCurrentUserQuery } from "../../../store/slices/api/authApi";

const Topbar = () => {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const user = useSelector(selectCurrentUser);
    const isAuthenticated = useSelector(selectIsAuthenticated);

    const { data: musician, isLoading } = useGetCurrentUserQuery(undefined, {
        skip: !isAuthenticated,
    });

    const handleLogout = () => {
        dispatch(logout());
        navigate("/login");
    };

    if (!isAuthenticated || !user) {
        return null;
    }

    const avatarUrl = musician?.avatar || "https://placecats.com/50/50";

    if (isLoading) {
        return (
            <Navbar fixed="top" className="d-lg-none w-100 bg-warning py-2">
                <Container fluid className="text-center">
                    <span>Caricamento...</span>
                </Container>
            </Navbar>
        );
    }

    return (
        <Navbar fixed="top" className="d-lg-none w-100 bg-warning py-2">
            <Container fluid className="px-3 justify-content-center ">
                <Row className="d-flex align-items-center w-100 justify-content-around">
                    <Col className="d-flex justify-content-center">
                        {isAuthenticated && (
                            <Button className="btn btn-sm btn-danger px-3 rounded-circle me-1" onClick={handleLogout}>
                                <i className="bi bi-door-open fs-6"></i>
                            </Button>
                        )}
                        <Image src={avatarUrl} alt="Profile" roundedCircle width={50} height={50} style={{ objectFit: "cover" }} />{" "}
                    </Col>

                    <Col className="lh-sm d-flex flex-column justify-content-center">
                        <span className="fw-bold text-dark fs-6">{user.username}</span>
                        <br />
                        <small className="text-dark fst-italic">
                            <a href="#">Il mio profilo</a>
                        </small>
                        <br />
                        <small className="text-dark fst-italic">
                            <a href="#">Le mie Jam</a>
                        </small>
                    </Col>
                    <Col className="d-flex justify-content-center">
                        <Button
                            variant="link"
                            className="text-dark d-flex align-items-center justify-content-center rounded-circle border border-dark"
                            style={{ width: "45px", height: "45px", minWidth: "45px" }}
                            aria-label="Search"
                        >
                            <i className="bi bi-search"></i>
                        </Button>
                    </Col>
                </Row>
            </Container>
        </Navbar>
    );
};

export default Topbar;
