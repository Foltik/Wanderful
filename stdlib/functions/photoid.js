const places = require('../places.js');

/**
* Returns a photo URL given a reference
* @param {string} reference Photo reference
* @param {number} max_width Maximum width of the photo
* @returns {object}
*/
module.exports = async (reference, max_width = 800, context) => {
    const photo = await places.getPhoto(reference, max_width);
    return {
        result: photo
    };
};
