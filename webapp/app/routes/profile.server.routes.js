// Invoke 'strict' JavaScript mode
'use strict';
var express = require('express'),
    router = express.Router(),
    profile = require('../controllers/profile.server.controller'),
    gateway = require('../../config/gateway'),
    braintree = require('braintree');

// Define the routes module' method

var TRANSACTION_SUCCESS_STATUSES = [
    braintree.Transaction.Status.Authorizing,
    braintree.Transaction.Status.Authorized,
    braintree.Transaction.Status.Settled,
    braintree.Transaction.Status.Settling,
    braintree.Transaction.Status.SettlementConfirmed,
    braintree.Transaction.Status.SettlementPending,
    braintree.Transaction.Status.SubmittedForSettlement
];
router.get('/', function (req, res, next) {
    res.send('respond with a resource');
});

/* GET user profile. */
router.get('/profile', function (req, res, next) {
    res.send(req.user);
});

router.post('/edit', profile.edit);

router.get('/checkouts/new', function (req, res) {
    gateway.clientToken.generate({}, function (err, response) {
      res.send({clientToken: response.clientToken, messages: "Success"});
    });
});


module.exports = router;

