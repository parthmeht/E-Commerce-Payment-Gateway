// Invoke 'strict' JavaScript mode
'use strict';

// Load the module dependencies

let User = require('mongoose').model('User'),
    config = require('../../config/config'),
    gateway = require('../../config/gateway'),
    braintree = require('braintree'),
    Transaction = require('mongoose').model('Transaction'),
    Item = require('mongoose').model('Item'),
    lodash = require('lodash');

let TRANSACTION_SUCCESS_STATUSES = [
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
        let message = null;
        let query = {'username':req.user.username};
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
    if (req.user){
        let transactionErrors;
        let amount = req.user.currentTransaction.totalAmount; // In production you should not take amounts directly from clients
        let nonce = req.body.payment_method_nonce;

        gateway.transaction.sale({
            amount: amount,
            paymentMethodNonce: nonce,
            options: {
                submitForSettlement: true
            }
        }, function (err, result) {
            if (result.success || result.transaction) {
                let query = {'username':req.user.username};
                req.user.currentTransaction.transactionId = result.transaction.id;
                let trans = {};
                trans.transactionId = result.transaction.id;
                trans.status = req.user.currentTransaction.status;
                trans.totalAmount = req.user.currentTransaction.totalAmount;
                req.user.transactionHistory.push(new Transaction(trans));
                /*req.user.currentTransaction.cartItems.forEach(function(element) {
                    console.log(element);
                    console.log(req.user.transactionHistory[req.user.transactionHistory.length-1]);
                    req.user.transactionHistory[req.user.transactionHistory.length-1].cartItems.push(new Item(element));
                });*/
                req.user.currentTransaction.totalAmount = 0;
                req.user.currentTransaction.cartItems = [];
                req.user.currentTransaction.transactionId = 0;
                User.update(query, req.user, function(err, doc){
                    if (err) return res.send(500, { error: err });
                    console.log(doc);
                    let message = "Your transaction is processed successfully!!";
                    return res.send({message});
                });
            } else {
                transactionErrors = result.errors.deepErrors();
                res.send(500,{message:transactionErrors});
            }
        });
    }
};

exports.addItem = function (req, res, next) {
    if(req.user){
        let query = {'username':req.user.username};
        let item = new Item(req.body);
        if(isNaN(req.user.currentTransaction.totalAmount))
            req.user.currentTransaction.totalAmount = item.discount;
        else{
            req.user.currentTransaction.totalAmount = req.user.currentTransaction.totalAmount + item.discount;
            req.user.currentTransaction.totalAmount = Math.round(req.user.currentTransaction.totalAmount * 100) / 100;
        }
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
      let cartLength = req.user.currentTransaction.cartItems.length;
      let query = {'username':req.user.username};
      let itemId = req.body.id;
      req.user.currentTransaction.cartItems = lodash.remove(req.user.currentTransaction.cartItems, function(obj) {
          return obj._id.toString() !== itemId;
      });
      if (cartLength===req.user.currentTransaction.cartItems.length){
          return res.send({message:"Item id not found in the cart"});
      }else{
          req.user.currentTransaction.totalAmount -= req.body.discountPrice;
          req.user.currentTransaction.totalAmount = Math.round(req.user.currentTransaction.totalAmount * 100) / 100;
          if (req.user.currentTransaction.totalAmount<0)
              req.user.currentTransaction.totalAmount = 0;
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
