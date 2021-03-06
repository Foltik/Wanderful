const lib = require('lib');
const places = require('../places.js');

const fieldMap = (array, field, fn) =>
      array.map((obj, index) => {
          obj[field] = fn(obj, index);
          return obj;
      });

const fieldSubmap = (array, field, fn) =>
      array.map(sub => sub.map(fieldMap(sub, field, fn)));

const flat = (array, depth = 1) =>
      array.reduce((flat, toFlatten) =>
                   flat.concat((Array.isArray(toFlatten) && (depth-1)) ? flat(toFlatten, depth - 1) : toFlatten));

/**
* Returns a travel itinerary of places around a central location.
* The order of places is determined by the categories and their weight.
* Example radius: `6000`
* Example center: `Tokyo, Japan`
* Example queries: `[{"query": "museum", weight: 7}, {"query": "restaurant", "weight": 9}]`
* Example num: `3`
* Example return: [Place, Place, ...] (See above)
* @param {number} radius How far around to search for points of interest in meters
* @param {string} center Location to center the search around
* @param {string} queries JSON serialized pairs of keywords with weight to find and sort points of interest
* @param {number} num Limit the maximum number of places
* @returns {object}
*/
module.exports = async (radius, center, queries, num, context) => {
    queries = JSON.parse(queries);

    // Get places 
    const max_places = 5;
    const weights = queries.map(q => q.weight);
    const weight_max = Math.max(...weights);
    const weight_min = Math.min(...weights);
    const weight_range = weight_max - weight_min;

    const trunc = num => num.toFixed(2);

    // Weight result by category weight
    const weightedResults = await Promise.all(queries.map(async q =>
        (await places.getPlaces(center, radius, q.query))
            .sort((a, b) => b.rating - a.rating)
            .slice(0, max_places)
            .map(p => {
                p.weight = q.weight;
                return p;
            })));

    // Decay weight by index
    const adjustedResults = weightedResults.map(query =>
        query.map((p, index) => {
            p.factor = (0.90 - (0.2 * ((p.weight - weight_min) / weight_range)));
            p.weight = p.weight * (p.factor ** (index + 1));
            return p;
        }));

    // Multiply weight by rating to get composite rating
    const results = flat(adjustedResults.map(query =>
        query.map(p => {
            p.weight = p.weight * p.rating;
            return p;
        }))).sort((a, b) => b.weight - a.weight);

    const finalPlaces = results.map(p => {
        return {
            id: p.id,
            name: p.name,
            rating: p.rating,
            types: p.types,
            address: p.formatted_address,
            icon: p.icon,
            photo: p.photos ? p.photos[0].photo_reference : undefined
        };
    });

    return {
        result: finalPlaces.slice(0, num)
    };
};
