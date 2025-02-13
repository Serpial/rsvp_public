function getMenuType(menuType) {
  switch (menuType) {
    case "STANDARD":
      return "Standard";
    case "VEGAN":
      return "Vegan";
    case "CHILDREN":
      return "Children's";
    default:
      break;
  }
  return;
}

export default { getMenuType };
