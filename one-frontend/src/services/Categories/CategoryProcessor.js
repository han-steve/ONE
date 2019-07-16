const fs = require("fs");

var json = require("./Plaid Categories.json");
var data = json.categories;
var final = [];
var currentLevel = final;
data.forEach(category => {
  category.hierarchy.forEach(hierarchy => {
    var found = undefined;
    for (let i = 0; i < currentLevel.length; i++) {
      if (currentLevel[i].name === hierarchy) {
        found = currentLevel[i];
      }
    }
    if (found) {
      if (!found.children) {
        found.children = [];
      }
      currentLevel = found.children;
    }
  });
  currentLevel.push({
    id: category.category_id,
    name: category.hierarchy[category.hierarchy.length - 1]
  });
  currentLevel = final;
});
var result = JSON.stringify(final, null, 2);

fs.writeFile("CategoryTree.json", result, err => {
  if (err) throw err;
});
