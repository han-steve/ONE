const fs = require("fs");

var json = require("../../../CategoryTree.json");
// var mapping = require("../../Map.json");
var map = {};
var final = [];

json.forEach(e => {
  var ids= []
  findSubCategories(e, ids);
  ids.forEach(id => {
    map[id] = "";
  });
});

console.log(map);

// mapping.forEach(e => {
//   console.log(e);
// });

var result = JSON.stringify(final, null, 2);

fs.writeFile("../../CategoryMap.json", result, err => {
  if (err) throw err;
});

function findSubCategories(root, result) {
  result.push(root.id);
  if (!root.children) {
    return;
  } else {
    root.children.forEach(el => {
      findSubCategories(el, result);
    });
  }
}