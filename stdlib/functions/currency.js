const axios = require('axios');

const api_url = 'https://restcountries.eu/rest/v2/alpha/';
const key = '&access_key=YOUR_API_KEY';

/**
* Returns the string ISO currency code for a given country.
* Example country: `"jpn"`
* Example return: `"JPY"`
* @param {string} country ISO country code to retrieve the primary currency of.
* @returns {object}
*/
module.exports = async (country, context) => {
    const res = (await axios.get(api_url + country)).data;
    return {
        result: res.currencies[0].code
    };
};
