const axios = require('axios');

const api_url = 'https://maps.googleapis.com/maps/api/place';
const key = '&key=YOUR_API_KEY';

const get_api = async resource =>
      axios.get(api_url + resource + key);

const get_api_noredirect = async resource =>
      axios.get(api_url + resource + key, {
          maxRedirects: 0
      });

const getCoordinates = async query => {
    const loc = (await get_api('/findplacefromtext/json'
        + '?input=' + query
        + '&inputtype=textquery'
        + '&fields=geometry')).data.candidates[0].geometry.location;

    return [loc.lat, loc.lng];
};

const getPlace = async query =>
      (await get_api('/findplacefromtext/json'
                     + '?input=' + query
                     + '&inputtype=textquery'
                     + '&fields=photos')).data.candidates[0];

const getPlaces = async (location, radius, query) =>
    (await get_api('/textsearch/json'
        + '?location=' + (await getCoordinates(location)).join(',')
        + '&radius=' + radius
        + '&query=' + encodeURIComponent(query))).data.results;

const getNearby = async (location, radius, type = null) =>
      (await get_api('/nearbysearch/json'
        + '?location=' + location
        + (type ? ('&type=' + type) : '')
        + '&opennow'
        + '&radius=' + radius)).data.results;
                     
                    
const getDetails = async place_id =>
    (await get_api('/details/json'
        + '?placeid=' + place_id
        + '&fields=opening_hours')).data.results;

const getPhoto = async (photo_id, max_width = 800) => {
    try {
        const res = (await get_api_noredirect('/photo'
        + '?maxwidth=' + max_width
        + '&photoreference=' + photo_id)).data;
        return res;
    } catch(err) {
        return err.response.headers.location;
    }
};

module.exports.getCoordinates = getCoordinates;
module.exports.getPlace = getPlace;
module.exports.getPlaces = getPlaces;
module.exports.getNearby = getNearby;
module.exports.getDetails = getDetails;
module.exports.getPhoto = getPhoto;
