var ortools = require('node_or_tools');

const fill = (n, val) =>
    [...Array(n)].map(() => val);

const fill2d = (n, val) =>
    [...Array(n)].map(() =>
        [...Array(n)].map(() => val));

const compositeDurations = (costs, durations) => {
    let composite = costs.slice(0);
    for (let i = 0; i < costs.length; i++) {
        for (let j = 0; j < costs.length; j++) {
            composite[i][j] += durations[i];
        }
    }
    return composite;
};


/** 
* Generate an optimized route between nodes, given a list of nodes
* and some constraints. The first node is assumed to be the starting and
* ending point.
* @param {number} delta_time Total time constraint
* @param {number} num_nodes Number of places
* @param {array} costs Array where [i][j] is the distance from i to j
* @param {array} durations Array of the time spent at i
* @param {array} windows Array of pairs of time windows for node i
* @returns {object}
*/

module.exports = (delta_time, num_nodes, costs, durations, windows) => {
    const createOpts = {
        numNodes: n,
        costs: costs,
        durations: compositeDurations(costs, durations),
        timeWindows: windows,
        demands: fill2d(n, 0)
    };

    const searchOpts = {
        computeTimeLimit: 10000,
        numVehicles: 1,
        depotNode: 0,
        timeHorizon: endTime,
        vehicleCapacity: 2147483647,
        routeLocks: [[]],
        pickups: [],
        deliveries: []
    };

    const vrp = new ortools.VRP(createOpts);

    return new Promise((resolve, reject) => 
        vrp.Solve(searchOpts, (err, res) =>
                  err ? reject(err) : resolve(res)));
};
