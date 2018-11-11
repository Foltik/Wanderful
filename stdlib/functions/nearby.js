/**
* Returns a list of nearby places
* @param {string} location Comma separated latitude and longitude
* @param {number} radius How far around to find places
* @param {number} num How many nearby places to get
* @param {string} type What type of place to find
* @returns {object}
*/

const places = require('../places.js');

module.exports = async (location = '', radius, num, type = null, context) => {
    let data = (await places.getNearby(location, radius, type));
    
    data = data.map(place => {
        return {
            id: place.id,
            name: place.name,
            rating: place.rating,
            types: place.types,
            address: place.vicinity,
            icon: place.icon,
            photo: place.photos ? place.photos[0].photo_reference : undefined
        };
    }).slice(0, num);

    return {
        result: data
    };
};
