import { Navbar, Container, Image, Button, Row, Col } from "react-bootstrap";

const Topbar = () => {
    return (
        <Navbar fixed="top" className="d-lg-none w-100 bg-warning py-2 h-50">
            <Container fluid className="px-3">
                <Row className="d-flex">
                    <Col>
                        <Image src="https://placecats.com/50/50" alt="Profile" roundedCircle width={50} height={50} style={{ objectFit: "cover" }} />{" "}
                    </Col>

                    <Col className="lh-sm">
                        <span className="fw-bold text-dark fs-6">UserName1234</span>
                        <small className="text-dark fst-italic">Il mio profilo</small>
                        <small className="text-dark fst-italic">Le mie fam</small>
                    </Col>
                    <Col>
                        <Button
                            variant="link"
                            className="p-0 border-0 text-dark d-flex align-items-center justify-content-center"
                            style={{ width: "45px", height: "45px", minWidth: "45px" }}
                            aria-label="Search"
                        >
                            Cerca
                        </Button>
                    </Col>
                </Row>
            </Container>
        </Navbar>
    );
};

export default Topbar;
