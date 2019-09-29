// Invoke 'strict' JavaScript mode
'use strict';

// Load the module dependencies
var User = require('mongoose').model('User'),
    config = require('../../config/config'),
    gateway = require('../../config/gateway'),
    braintree = require('braintree');

    var TRANSACTION_SUCCESS_STATUSES = [
        braintree.Transaction.Status.Authorizing,
        braintree.Transaction.Status.Authorized,
        braintree.Transaction.Status.Settled,
        braintree.Transaction.Status.Settling,
        braintree.Transaction.Status.SettlementConfirmed,
        braintree.Transaction.Status.SettlementPending,
        braintree.Transaction.Status.SubmittedForSettlement
    ];
    
exports.edit = function(req, res, next) {
    if (req.user) {
        //var user = new User(req.body);
        var message = null;
        var query = {'username':req.user.username};
        req.user.firstName = req.body.firstName;
        req.user.lastName = req.body.lastName;
        req.user.city = req.body.city;
        req.user.gender = req.body.gender;
        User.update(query, req.user, function(err, doc){
            if (err) return res.send(500, { error: err });
            message = "Profile updated succesfully!!";
            return res.send({message});
        });
    }
};

exports.getClientToken = function(req, res, next) {
    gateway.clientToken.generate({}, function (err, response) {
        res.send({clientToken: response.clientToken, messages: "Success"});
    });
};

exports.checkout = function(req, res, next){
    var nonceFromTheClient = req.body.payment_method_nonce;

};