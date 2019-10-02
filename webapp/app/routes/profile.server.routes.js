// Invoke 'strict' JavaScript mode
'use strict';
var express = require('express'),
    router = express.Router(),
    profile = require('../controllers/profile.server.controller');

// Define the routes module' method

router.get('/', function (req, res, next) {
    res.send('respond with a resource');
});

/* GET user profile. */
router.get('/profile', function (req, res, next) {
    res.send(req.user);
});

router.post('/edit', profile.edit);

router.get('/client_token', profile.getClientToken);

router.post("/checkout", profile.checkout);

router.post("/addItem", profile.addItem);

router.post("/deleteItem", profile.deleteItem);

module.exports = router;

