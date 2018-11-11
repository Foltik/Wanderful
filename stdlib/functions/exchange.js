const lib = require('lib');
const axios = require('axios');

const api_url = 'http://data.fixer.io/api/latest';
const key = '?access_key=YOUR_API_KEY';

/**
* Returns the float exchange rate between two currencies.
* The target is passed as a country code.
* The API converts this country code to the primary currency of that country.
* Example from: `"USD"`
* Example to: `"jpn"`
* Example return: `113.83`
* @param {string} from ISO currency code for the starting currency
* @param {string} to ISO country code with which to determine target currency code 
* @returns {object}
*/
module.exports = async (from, to, context) => {
    to = (await lib[`${context.service.identifier}.currency`](to)).result;

    const data = (await axios.get(api_url + key
                                  + '&base=EUR')).data.rates;

    const eur_to_source = data[from];
    const eur_to_dest = data[to];

    return {
        result: eur_to_dest / eur_to_source
    };
};
