/**
* Returns something that could resemble an itinerary
* @param {number} radius How far around to build the itinerary
* @param {string} center Where to build the itinerary around
* @param {string} home Where to start the itinerary
* @param {array} queries Keywords to find points of interest
* @param {number} start_time When to start the day
* @param {number} end_time When to end the day
* @param {string} date What day to plan the itinerary
* @param {number} num How many places to return
* @returns {object}
*/

const lib = require('lib');
const directions = require('../directions.js');
const places = require('../places.js');

const fieldMap = (array, field, fn) =>
      array.map((obj, index) => {
          obj[field] = fn(obj, index);
          return obj;
      });

const fieldSubmap = (array, field, fn) =>
      array.map(sub => sub.map(fieldMap(sub, field, fn)));

module.exports = async (radius = 0, center, home, queries, start_time, end_time, date, num, context) => {
    queries = JSON.parse(queries);
    
    // Get places 
    const max_places = 5;
    const weights = queries.map(q => q.weight);
    const weight_max = Math.max(...weights);
    const weight_min = Math.min(...weights);
    const weight_range = weight_max - weight_min;

    const trunc = num => num.toFixed(2);

    const weightedResults = await Promise.all(queries.map(async q =>
        (await places.getPlaces(center, radius, q.query))
            .sort((a, b) => b.rating - a.rating)
            .slice(0, max_places)
            .map(p => {
                p.weight = q.weight;
                return p;
            })));
    const weightOnly = weightedResults.flat().map(p => `${trunc(p.weight)}:${trunc(p.rating)} -> ${p.name}`);

    // Decay weight by index
    const adjustedResults = weightedResults.map(query =>
        query.map((p, index) => {
            p.factor = (0.90 - (0.2 * ((p.weight - weight_min) / weight_range)));
            p.weight = p.weight * (p.factor ** (index + 1));
            return p;
        }));
    const decayOnly = adjustedResults.flat().map(p => `${trunc(p.weight)}:${trunc(p.factor)} -> ${p.name}`);

    // Multiply weight by rating to get composite rating
    const results = adjustedResults.map(query =>
        query.map(p => {
            p.weight = p.weight * p.rating;
            return p;
        })).flat().sort((a, b) => b.weight - a.weight);
    const resultsWeight = results.map(p => `${trunc(p.weight)}:${trunc(p.rating)} -> ${p.name}`);

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
        params: context.params,
        weightOnly: weightOnly,
        decayOnly: decayOnly,
        resultsWeight: resultsWeight,
        results: finalPlaces.slice(0, num)
    };
};
