const places = require('../places.js');

/**
* Returns a photo URL given a reference
* Example reference: `"5hqea57jc7Kk1XGhJnqQiCridfo_r4"` (Gotten from Place API)
* Example max_width: `800`
* Example return: `"https://lh3.googleusercontent.com/p/AF1QipONQIjPH65hq"`
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
