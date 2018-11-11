/**
* Returns the exchange rate between countries
* @param {string} from ISO currency code for the starting currency
* @param {string} to ISO country code that gets converted to the target currency code
* @returns {object}
*/

const lib = require('lib');
const axios = require('axios');

const api_url = 'http://data.fixer.io/api/latest';
const key = '?access_key=4d764507f666731a05a6ff3411aae672';

module.exports = async (from = '', to, context) => {
    to = (await lib[`${context.service.identifier}.currency`](to)).result;

    const data = (await axios.get(api_url + key
                                  + '&base=EUR')).data.rates;

    const eur_to_source = data[from];
    const eur_to_dest = data[to];

    return {
        result: eur_to_dest / eur_to_source
    };
};
