import { useEffect, useState } from "react";

import "./Cat.css";

function Cat() {
  const [fact, setFact] = useState("");

  useEffect(() => {
    fetch("https://catfact.ninja/fact", {
      method: "GET",
      headers: {
        ContentType: "/application/json",
      },
    })
      .then((response) => response.json())
      .then((data) => setFact(data?.fact))
      .catch((error) => console.error(error));
  }, []);

  return (
    <section className="cat__section">
      <p>{fact}</p>
    </section>
  );
}

export default Cat;
