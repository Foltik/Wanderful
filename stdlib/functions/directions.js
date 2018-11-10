const axios = require('axios');

const app_url = 'https://maps.googleapis.com/maps/api';
const key = '&key=AIzaSyCL2k612OOVYYxrqP2j7t1ty8nANPpQlPE';

const get_api = async resource =>
      (await axios.get(app_url + resource + key));

const getDirections = async (origin, destination, time) =>
      (await get_api('/directions/json'
                     + '?origin=' + origin
                     + '&dest=' + dest
                     + '&mode=transit'
                     + '&departure_time=' + time)).routes[0];

const makeLocationString = places =>
      places.map(p => p.geometry.location.lat + ',' + p.geometry.location.lng).join('|');

const makeDurationArray = matrix =>
      matrix.map(row => row.map(elem => elem.duration.value / 3600));
      //matrix.map(row => row.map(elem => elem.duration ? elem.duration.value / 3600 : 0));

const getDurationMatrix = async (places, time) =>
      makeDurationArray(await get_api('/distancematrix/json'
              + '?origins=' + makeLocationString(places)
              + '&units=imperial'
              + '&destinations=' + makeLocationString(places)
              + '&departure_time' + time
              + '&mode=transit'));

module.exports.getDirections = getDirections;
module.exports.getDurationMatrix = getDurationMatrix;
