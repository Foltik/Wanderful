/**
* Returns something that could resemble an itinerary
* @param {string} center Where to build the itinerary around
* @param {string} home Where to start the itinerary
* @param {number} radius How far around to build the itinerary
* @param {array} queries Keywords to find points of interest
* @param {string} start_time When to start the day
* @param {string} end_time When to end the day
* @param {string} date What day to plan the itinerary
* @returns {object}
*/

const directions = require('directions.js');
const places = require('places.js');
const optimize = require('optimize.js');

module.exports = async (center, radius, queries, home, start_time, end_time, date) => {
    
};
