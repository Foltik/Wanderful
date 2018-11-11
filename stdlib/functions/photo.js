const places = require('../places.js');

/**
* Returns a photo URL from a text search
* Example place: `"Tokyo"`
* Example return: "https://lh3.googleusercontent.com/p/AF1QipONQIjPH65hqea57"
* @param {string} place Query string
* @returns {object}
*/
module.exports = async (place, context) => {
    place = await places.getPlace(place);
    const photo_reference = place.photos[0].photo_reference;
    const photo = await places.getPhoto(photo_reference, 800);
    return {
        result: photo
    };
};
