# Wanderful API

Have you ever been planning for a trip well before hand, and realized
that you were spending more time planning than travelling? 
Wanderful is a simple and easy to use service that alleviates the
stress and removes the unknowns from planning a trip.
It can plan your trip down to the hour, customizing your trip
based on a variety of contextual information such as location hours,
hotel availability, and nearby events.

Additionally, through the power of Standard Library, you can now integrate 
rich contextual data into your own projects and applications using the Wanderful API.

The format of each API call is simple. Give the parameters documented below, a
JSON object with the key `"request"` containing the target data is returned.

For example, retrieving the currency code for a country is extremely simple with
our API.
```javascript
const axios = require('axios');

const request = await axios.get('https://foltik.lib.id/itinegen@2.0.0/currency?country=jpn');
const language = request.data.result; // "JPY"
```

Some of our API endpoints return a list of places, each of which contains
rich metadata about the place, taking into account timezones location hours.

A Place object provides the following fields:
```javascript
{
    id,      // Place identifier for explicit usage with our API
    name,    // Localized name of the place
    rating,  // Google Maps rating of the place
    types,   // Array of types of the place, eg. `["park", "point_of_interest"]`
    address, // Formatted address of the place
    icon,    // Icon URL corresponding to the place's type
    photo    // Photo reference for retrieving photos of the place
}
```
