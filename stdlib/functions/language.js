const axios = require('axios');

const api_url = 'https://restcountries.eu/rest/v2/alpha/';
const key = '&access_key=YOUR_API_KEY';

/**
* Returns the string ISO language code, given an ISO country code.
* Example country: `"usa"`
* Example return: `"en"`
* @param {string} country ISO country code
* @returns {object}
*/
module.exports = async (country, context) => {
    const res = (await axios.get(api_url + country)).data;
    return {
        result: res.languages[0].iso639_1
    };
};
