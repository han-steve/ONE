import dataJSON from "test.json";

function formatData(data) {
  return {
    name: data.name,
    item_name: data.menus.name,
    active: false,
    coordinate: [data.longitude, data.latitude],
    cuisines: data.cuisines.split("\\s*,\\s*")
    // ,
    // ingredients: []
  };
}

function readData() {
  var result = [];
  dataJSON.forEach((item) => {

  });
  return result;
}

// function valid(item) {
//
// }













function findCategory(id /* '13005032' */, categoryTree) {
  var firstLevel = +id.substring(0, 2);
  var secondLevel = +id.substring(3, 5);
  var thirdLevel = +id.substring(6, 8);
  var result = null;
  if (firstLevel) {
    result = categoryTree[firstLevel - 10];
    if (secondLevel) {
      result = result.children[secondLevel - 1];
      if (thirdLevel) {
        result = result.children[thirdLevel - 1];
      }
    }
  }
  return result;
}
function populateTreeWithValues(transactions, categoryTree) {
  transactions.forEach(transaction => {
    var category = findCategory(transaction.category_id, categoryTree);
    var amount = Math.abs(transaction.amount);
    if (!category.value) {
      category.value = amount;
    } else {
      category.value += amount;
    }
  });
}
function calcSum(root) {
  var sum = root.value;
  if (root.children) {
    root.children.forEach(child => {
      sum += calcSum(child);
    });
  }
  root.value = sum;
}
function updateSums(root) {
  if (!root.children) {
    if (!root.value) {
      root.value = 0;
    }
    return root.value;
  }
  var sum = 0;
  root.children.forEach(child => {
    sum += updateSums(child);
  });
  if (root.value) {
    root.value += sum;
  } else {
    root.value = sum;
  }
  return root.value;
}
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
export default {
  findCategory,
  populateTreeWithValues,
  calcSum,
  updateSums,
  findSubCategories
};
