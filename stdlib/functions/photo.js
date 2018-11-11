/**
* Returns a photo URL given a query string
* @param {string} place Query for the place
* @returns {object}
*/

const places = require('../places.js');

module.exports = async (place = '', context) => {
    place = await places.getPlace(place);
    const photo_reference = place.photos[0].photo_reference;
    const photo = await places.getPhoto(photo_reference, 800);
    return {
        result: photo
    };
};
