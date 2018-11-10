const axios = require('axios');

const api_url = 'https://maps.googleapis.com/maps/api/place';
const key = '&key=AIzaSyCL2k612OOVYYxrqP2j7t1ty8nANPpQlPE';

const get_api = async resource =>
      axios.get(api_url + resource + key);

const getCoordinates = async query => {
    const loc = (await get_api('/findplacefromtext/json'
        + '?input=' + query
        + '&inputtype=textquery'
        + '&fields=geometry'
        + key)).data.candidates[0].geometry.location;

    return [loc.lat, loc.lng];
};

const getPlaces = async (location, radius, query) =>
    (await get_api('/textsearch/json'
        + '?location=' + (await getCoordinates(location)).join(',')
        + '&radius=' + radius
        + '&query=' + encodeURIComponent(query))).data.results;

const getDetails = async place_id =>
    (await get_api('/details/json'
        + '?placeid=' + place_id
        + '&fields=opening_hours')).data.results;

const getPhoto = async (photo_id, max_height = 10000) =>
    (await get_api('/photo'
        + '?maxheight=' + max_height
        + '&photoreference=' + photo_id)).data;

module.exports.getCoordinates = getCoordinates;
module.exports.getPlaces = getPlaces;
module.exports.getDetails = getDetails;
module.exports.getPhoto = getPhoto;
