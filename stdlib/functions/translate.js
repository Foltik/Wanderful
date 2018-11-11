/**
* Returns a translated string
* @param {string} from ISO language code of starting language
* @param {string} to ISO country code, coverted to destination language code
* @param {string} str String to translate
* @returns {object}
*/

const lib = require('lib');
const axios = require('axios');

const api_url = 'https://translation.googleapis.com/language/translate/v2';
const key = '&key=AIzaSyCVktAWTsOabuB_YAZdLznuEtyiZnbUlss';

module.exports = async (from = '', to, str, context) => {
    to = (await lib[`${context.service.identifier}.language`](to)).result;

    const res = (await axios.get(api_url
        + '?q=' + str
        + '&source=' + from
        + '&target=' + to
        + '&format=text'
        + key)).data.data;

    return {
        result: res.translations[0].translatedText
    };
};
