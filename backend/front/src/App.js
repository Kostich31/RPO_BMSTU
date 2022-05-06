import './App.css';
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { connect } from "react-redux";
import NavigationBar from "./components/NavigationBarClass";
import Home from "./components/Home";
import Login from "./components/Login";
import SideBar from "./components/SideBar";
import CountryListComponent from "./components/CountryListComponent";
import CountryComponent from "./components/CountryComponent";
import Utils from "./Utils/Utils";
import { useState } from "react";

const ProtectedRoute = ({ children }) => {
    let user = Utils.getUser();
    return user ? children : <Navigate to={'/login'} />
}

function App(props) {
    const [exp, setExpanded] = useState(true);

    return (
        <div className="App">
            <BrowserRouter>
                <NavigationBar toggleSideBar={() => setExpanded(!exp)} />
                <div className="wrapper">
                    <SideBar expanded={exp} />
                    <div className="container-fluid">
                        {props.error_message &&
                            <div className="alert alert-danger m-1">{props.error_message}</div>
                        }
                        <Routes>
                            <Route path="login" element={<Login />} />
                            <Route path="home" element={<ProtectedRoute><Home /></ProtectedRoute>} />
                            <Route path="countries" element={<ProtectedRoute><CountryListComponent /></ProtectedRoute>} />
                            <Route path="countries/:id" element={<ProtectedRoute><CountryComponent /></ProtectedRoute>} />
                        </Routes>
                    </div>
                </div>
            </BrowserRouter>
        </div>
    );
}
function mapStateToProps(state) {
    const { msg } = state.alert;
    return { error_message: msg };
}
export default connect(mapStateToProps)(App);
