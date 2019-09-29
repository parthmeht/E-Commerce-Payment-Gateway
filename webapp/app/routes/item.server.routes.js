// Invoke 'strict' JavaScript mode
'use strict';
var express = require('express'),
    router = express.Router(),
    item = require('../controllers/item.server.controller');

router.get('/getItems', item.getItems);

module.exports = router;