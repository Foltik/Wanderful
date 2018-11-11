/**
* Returns the ISO language code for a country
* @param {string} country ISO country code
* @returns {object}
*/

const axios = require('axios');

const api_url = 'https://restcountries.eu/rest/v2/alpha/';
const key = '&access_key=4d764507f666731a05a6ff3411aae672';

module.exports = async (country = '', context) => {
    const res = (await axios.get(api_url + country)).data;
    return {
        result: res.languages[0].iso639_1
    };
};
