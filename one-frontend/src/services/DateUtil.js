function getToday() {
  var today = new Date();
  today.setHours(0, 0, 0, 0);
  return today;
}
function getThisMonth() {
  var day = getToday();
  day.setDate(1);
  return day;
}
function getPastMonth() {
  var day = getToday();
  day.setMonth(day.getMonth() - 1);
  return day;
}
function getThisYear() {
  var day = getToday();
  day.setMonth(0);
  day.setDate(1);
  return day;
}
function getPastYear() {
  var day = getToday();
  day.setFullYear(day.getFullYear() - 1);
  return day;
}
function getEarliestDate() {
  var day = new Date(0);
  day.setHours(0, 0, 0, 0);
  return day;
}
function parseDate(dateString) {
  //'2019-07-11'
  var year = +dateString.substring(0, 4);
  var month = +dateString.substring(5, 7);
  var day = +dateString.substring(8, 10);
  return new Date(year, month - 1, day);
}
function stringifyDate(date) {
  var year = date.getFullYear();
  var month = date.getMonth() + 1;
  var day = date.getDate();
  return year + "-" + month + "-" + day;
}
export default {
  getToday,
  getThisMonth,
  getPastMonth,
  getThisYear,
  getPastYear,
  getEarliestDate,
  parseDate,
  stringifyDate
};
