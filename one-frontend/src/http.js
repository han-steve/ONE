var httpGetOptions = function() {
  return {
    // body: JSON.stringify(payload), // must match 'Content-Type' header
    cache: "no-cache",
    // mode: 'cors',
    // credentials: 'same-origin', // include, same-origin, *omit
    // headers: {
    //     // 'Content-Type': 'application/json',
    //     'Accept': 'application/json'
    // },
    method: "GET" // *GET, POST, PUT, DELETE, etc.
    // mode: 'cors', // no-cors, cors, *same-origin
    // referrer: 'no-referrer' // *client, no-referrer
  };
};

export { httpGetOptions };

var httpPostOptions = function(payload) {
  return {
    body: JSON.stringify(payload), // must match 'Content-Type' header
    cache: "no-cache",
    // credentials: 'same-origin', // include, same-origin, *omit
    headers: {
      "Content-Type": "application/json"
    },
    method: "POST" // *GET, POST, PUT, DELETE, etc.
    // mode: 'cors', // no-cors, cors, *same-origin
    // referrer: 'no-referrer' // *client, no-referrer
  };
};
export { httpPostOptions };

var httpDeleteOptions = function(payload) {
  return {
    body: JSON.stringify(payload), // must match 'Content-Type' header
    cache: "no-cache",
    // credentials: 'same-origin', // include, same-origin, *omit
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json"
    },
    method: "DELETE" // *GET, POST, PUT, DELETE, etc.
    // mode: 'cors', // no-cors, cors, *same-origin
    // referrer: 'no-referrer' // *client, no-referrer
  };
};

export { httpDeleteOptions };

var httpPutOptions = function(payload) {
  return {
    body: JSON.stringify(payload), // must match 'Content-Type' header
    cache: "no-cache",
    // credentials: 'same-origin', // include, same-origin, *omit
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json"
    },
    method: "PUT" // *GET, POST, PUT, DELETE, etc.
    // mode: 'cors', // no-cors, cors, *same-origin
    // referrer: 'no-referrer' // *client, no-referrer
  };
};

export { httpPutOptions };
