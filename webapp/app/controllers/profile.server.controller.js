// Invoke 'strict' JavaScript mode
'use strict';

// Load the module dependencies

var User = require('mongoose').model('User'),
    config = require('../../config/config'),
    gateway = require('../../config/gateway'),
    braintree = require('braintree'),
    Transaction = require('mongoose').model('Transaction'),
    Item = require('mongoose').model('Item'),
    lodash = require('lodash');

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
    if (req.user) {
        gateway.clientToken.generate({customerId: req.user.customerId}, function (err, response) {
            res.send({clientToken: response.clientToken, messages: "Success"});
        });
    }else{
        res.send({message: "Invalid Request"});
    }
};

exports.checkout = function(req, res, next){
    var nonceFromTheClient = req.body.payment_method_nonce;

};

exports.addItem = function (req, res, next) {
    if(req.user){
        let query = {'username':req.user.username};
        let item = new Item(req.body);
        if(isNaN(req.user.currentTransaction.totalAmount))
            req.user.currentTransaction.totalAmount = item.discountPrice;
        else
            req.user.currentTransaction.totalAmount += item.discountPrice;
        req.user.currentTransaction.cartItems.push(item);
        User.update(query, req.user, function(err, doc){
            if (err) return res.send(500, { error: err });
            console.log(doc);
            let message = "Cart updated successfully!!";
            return res.send({message});
        });
    }
};

exports.deleteItem = function (req,res, next) {
  if (req.user && req.user.currentTransaction!==undefined && req.user.currentTransaction.cartItems.length>0){
      let cartLength = req.user.currentTransaction.cartItems.length
      let query = {'username':req.user.username};
      let itemId = req.body.id;
      req.user.currentTransaction.cartItems = lodash.remove(req.user.currentTransaction.cartItems, function(obj) {
          return obj._id.toString() !== itemId;
      });
      if (cartLength===req.user.currentTransaction.cartItems.length){
          return res.send({message:"Item id not found in the cart"});
      }else{
          req.user.currentTransaction.totalAmount -= req.body.discountPrice;
          User.update(query, req.user, function(err, doc){
              if (err) return res.send(500, { error: err });
              console.log(doc);
              let message = "Cart updated successfully!!";
              return res.send({message});
          });
      }
  }else{
      return res.send(500,{message:"Can't perform this operation"});
  }
};
