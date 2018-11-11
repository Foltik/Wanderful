/**
* Returns a photo URL given a reference
* @param {string} reference Photo reference to lookup
* @param {number} max_width Maximum width
* @returns {object}
*/

const places = require('../places.js');

module.exports = async (reference = '', max_width = 800, context) => {
    const photo = await places.getPhoto(reference, max_width);
    return {
        result: photo
    };
};
