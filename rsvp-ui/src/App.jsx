import { Routes, Route, Link } from "react-router-dom";

import MainHeader from "./shared/MainHeader";
import MainFooter from "./shared/MainFooter";
import Home from "./home/Home";
import Response from "./response/Response";
import Cat from "./cat/Cat";
import AuthContextProvider from "./shared/contexts/AuthContextProvider";

import "./App.css";

export default function App() {
  return (
    <AuthContextProvider>
      <MainHeader />
      <main>
        <Routes>
          <Route path="/">
            {/* <Route index element={<Home />} /> */}
            {/* <Route path="with" element={<Home />} /> */}
            {/* <Route path="response" element={<Response />} /> */}
            <Route index element={<Sunset />} />
            <Route path="cat" element={<Cat />} />
            <Route path="page-not-found" element={<NoMatch />} />
            <Route path="*" element={<NoMatch />} />
          </Route>
        </Routes>
      </main>
      <MainFooter />
    </AuthContextProvider>
  );
}

function Sunset() {
  return (
    <div
      style={{
        maxWidth: "fit-content",
        margin: "5rem auto",
        marginLeft: "auto",
        marginRight: "auto",
      }}>
        <p>Thank you for coming to the wedding!</p>
      </div>
  )
}

function NoMatch() {
  return (
    <div
      style={{
        maxWidth: "fit-content",
        margin: "5rem auto",
        marginLeft: "auto",
        marginRight: "auto",
      }}
    >
      <h2>Nothing to see here!</h2>
      <p style={{ width: "100%", textAlign: "center" }}>
        <Link to="/">Go to the home page.</Link>
      </p>
    </div>
  );
}
