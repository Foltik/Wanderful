const axios = require('axios');

const api_url = 'https://maps.googleapis.com/maps/api/place';
const key = '&key=AIzaSyCL2k612OOVYYxrqP2j7t1ty8nANPpQlPE';

const get_api = async resource =>
      axios.get(app_url + resource + key);

const getPlaces = async (location, radius, query) =>
      (await get_api('/textsearch/json'
              + '?location=' + location[0] + ',' + location[1]
              + '&radius=' + radius
              + '&query=' + encodeURIComponent(query))).data.results;

const getDetails = async place_id =>
      (await get_api('/details/json'
              + '?placeid=' + place_id
              + '&fields=opening_hours')).data.results;

const getPhoto = async (photo_id, max_height) =>
      await get_api('/photo'
              + '?maxheight=' + max_height
              + '&photoreference=' + photo_id);

module.exports.getPlaces = getPlaces;
module.exports.getDetails = getDetails;
module.exports.getPhoto = getPhoto;
