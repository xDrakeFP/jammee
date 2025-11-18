import Topbar from "./TopBar";

const MainLayout = ({ children }) => {
    return (
        <>
            <Topbar />
            {children}
        </>
    );
};

export default MainLayout;
