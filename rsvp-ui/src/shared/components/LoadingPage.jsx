import LoadingWheel from "./LoadingWheel";

/*
 * Gif By Krishprakash24gmail - Own work, CC BY-SA 4.0, https://commons.wikimedia.org/w/index.php?curid=66593740
 */
function LoadingPage() {
  return (
    <div
      style={{
        width: "100vw",
        height: "100vh",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
      }}
    >
      <LoadingWheel />
    </div>
  );
}

export default LoadingPage;
