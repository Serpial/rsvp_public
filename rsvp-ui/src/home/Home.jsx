import LeafletMapWrapper from "./components/LeafletMapWrapper";
import TransferTo from "./components/TransferTo";
import LoginCard from "./components/LoginCard";
import markerIconPng from "leaflet/dist/images/marker-icon.png";
import { Icon } from "leaflet";
import { Marker, Popup } from "react-leaflet";
import MenuOptions from "./components/MenuOptions";

import "./Home.css";

function Home() {
  const mapsUri = "https://maps.app.goo.gl/QTpQg35NH3csbhTd9";
  const position = { longitude: 56.0377, latitude: -3.8463 };

  return (
    <>
      <section className="home-section home-section__when">
        <h3>What time should I arrive?</h3>
        <p>
          If you can, please arrive after 12pm. The ceremony should commence at
          1pm.
        </p>
        <div>
          <h4>Schedule</h4>
          <ul>
            <li>Ceremony - 1pm</li>
            <li>Canapés - 2pm</li>
            <li>Wedding breakfast - 5pm</li>
            <li>Evening reception - 8pm</li>
            <li>Evening buffet - 9:30pm</li>
            <li>Close of the evening reception and carriages - 12am</li>
          </ul>
        </div>
      </section>
      <section className="home-section home-section__rsvp">
        <h3>Can I RSVP?</h3>
        <LoginCard />
      </section>
      <section className="home-section home-section__eating">
        <h3>What are we eating?</h3>
        <div className="home-section__eating-canapes">
          <h4>Canapés</h4>
          <ul>
            <li>Sun blush Tomato Pesto & Mozzarella Croute</li>
            <li>Tandoori Chicken Poppadum</li>
            <li>Parma Ham & Tomato Tapenade Toasts</li>
          </ul>
        </div>
        <MenuOptions />
      </section>
      <section className="home-section home-section__where">
        <LeafletMapWrapper
          latitude={position.latitude}
          longitude={position.longitude}
        >
          <Marker
            position={[position.longitude, position.latitude]}
            icon={
              new Icon({
                iconUrl: markerIconPng,
                iconSize: [25, 41],
                iconAnchor: [12, 41],
              })
            }
          >
            <Popup>
              Glenbervie House,{" "}
              <a href={mapsUri} target="_blank">
                Open in Google Maps
              </a>
            </Popup>
          </Marker>
          <TransferTo uri={mapsUri} />
        </LeafletMapWrapper>
        <div className="home-section__where-content">
          <h3>Where is the wedding?</h3>
          <p>
            Join us at{" "}
            <span className="home-section__span--bold">
              Glenbervie House & Country Estate
            </span>
            . Both the wedding and reception will be held here on the day.
          </p>
          <p>
            The venue is easily accessible via the M876 or its a short taxi
            journey away from{" "}
            <a
              href="https://www.nationalrail.co.uk/stations/larbert/"
              target="_blank"
            >
              Larbert train station
            </a>
            .
          </p>
          <p>
            The full address for the venue is{" "}
            <span className="home-section__span--bold">
              Glenbervie House & Country Estate, Stirling Rd, Larbert FK5 4SJ
            </span>
          </p>
          <p>Click the map to open Google maps.</p>
        </div>
      </section>
      <section className="home-section home-section__accommodation">
        <h3>Where can I stay?</h3>
        <div className="home-section__accommodation-content">
          <p>
            Unfortunately, there is limited space at the venue. We will let you
            know if we can offer it to you. Thankfully, there are plenty of
            great and inexpensive options nearby:
          </p>
          <ul>
            <li>
              The closest option is easily the{" "}
              <a
                target="_blank"
                href="https://www.premierinn.com/gb/en/hotels/scotland/central/falkirk/falkirk-larbert.html"
              >
                Premier Inn, Larbert
              </a>
              . It&apos;s just a short 1.1 miles (1.7 km) away.
            </li>
            <li>
              Another great choice is the{" "}
              <a
                target="_blank"
                href="https://www.premierinn.com/gb/en/hotels/scotland/central/falkirk/falkirk-north.html"
              >
                Premier Inn near Kincardine
              </a>
              , which should also be available at a very similar price.
              It&apos;s about a 7-minute car journey away, approximately 4.5
              miles (7 km).
            </li>
            <li>
              The{" "}
              <a
                target="_blank"
                href="https://www.travelodge.co.uk/hotels/222/Stirling-M80-hotel"
              >
                Travelodge
              </a>{" "}
              at the edge of Stirling is also a good option. It&apos;s around 11
              minutes away, approximately 5 miles (8 km).
            </li>
          </ul>
        </div>
      </section>
    </>
  );
}

export default Home;
