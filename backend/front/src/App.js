import './App.css';
import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import NavigationBar from "./components/NavigationBarClass";
import Home from "./components/Home";
import AnotherHome from './components/AnotherHome';

function App() {
  return (
      <div className="App">
          <BrowserRouter>
              <NavigationBar/>
              <div className="container-fluid">
                  <Routes>
                      <Route path="home" element={<Home />} />
                      <Route path="home2" element={<AnotherHome />} />
                  </Routes>
              </div>
          </BrowserRouter>
      </div>
  );
}

export default App;
