import dateUtil from "./DateUtil.js";

export const defaultFilter = {
  date: {
    start: dateUtil.getThisMonth(),
    end: dateUtil.getToday()
  },
  accounts: [],
  categories: [],
  payees: [],
  type: "expense"
};
