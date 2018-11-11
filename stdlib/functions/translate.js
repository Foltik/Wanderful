const lib = require('lib');
const axios = require('axios');

const api_url = 'https://translation.googleapis.com/language/translate/v2';
const key = '&key=YOUR_API_KEY';

/**
* Translates a string between different languages.
* Example from: `"en"`
* Example to: `"jpn"`
* Example str: `"Hello!"`
* Example return: `"こんにちは！"`
* @param {string} from ISO language code of starting language
* @param {string} to ISO country code of destination country which is converted to a language code
* @param {string} str String to translate
* @returns {object}
*/
module.exports = async (from, to, str, context) => {
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
