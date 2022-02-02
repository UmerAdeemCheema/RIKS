var UserAuth = require('../models/userauth')
var User = require('../models/user')
var registrationToken = require('../models/registrationtoken')
var Index = require('../models/userIndex')
var InsuancePackage = require('../models/insurancepackages')
var InProcessInsuancePackage = require('../models/inprocessinsurancepackages')
var EmployeeActiveCases = require('../models/employeeactivecases')
var RegisteredInsuancePackage = require('../models/registeredinsurancepackages')
var UserFile = require('../models/userfile')


var jwt = require('jwt-simple')
var config = require('../config/dbconfig')
var fs = require('fs');
const { findOne } = require('../models/userauth')
var bcrypt = require('bcrypt')
const { Console } = require('console')


var functions =  {
    addNewUser: function (req,res) {
        if((!req.body.type) || (!req.body.email) || (!req.body.password) || (!req.body.username) || (!req.body.displayname) || (!req.body.cnic) || (!req.body.address) || (!req.body.contactno) ){
            return res.status(200).json({success:false, msg: 'Enter all Fields'})
        }
        else{
            if(req.body.type == "Employee" || req.body.type == "Manager"){
                if(!req.body.registrationtoken){
                    return res.status(200).json({success:false, msg: 'Enter all Fields'})
                }
                else{
                    registrationToken.findOne({
                        email: req.body.email
                    }, function (err, usertoken) {
                        if(err) return res.status(200).send({success: false, msg: "Error"});
                        if(usertoken) {
                            if(usertoken['registrationtoken'] == req.body.registrationtoken){
                                registrationToken.deleteOne( 
                                    { "email" : req.body.email },
                                    function (err,token){
                                        if(err) return res.status(200).send({success: false, msg: "Error"});
                                        if(token) {
                                            UserAuth.findOne({
                                                email: req.body.email
                                            }, function (err, user) {
                                                if(err) return res.status(200).send({success: false, msg: "Error"});
                                                if(user) {
                                                    return res.status(200).send({success: false, msg: "Email Already exists, Change Email Address"})
                                                }
                                                else {
                                                    User.findOne({
                                                        username: req.body.username
                                                    }, function (err, user) {
                                                        if(err) return res.status(200).send({success: false, msg: "Error"});
                                                        if(user) {
                                                            return res.status(200).send({success: false, msg: "Username Already exists, Change Username"})
                                                        }
                                                        else {
                                                            var newUserAuth = UserAuth({
                                                                email: req.body.email,
                                                                password: req.body.password
                                                            });
                                                            newUserAuth.save(function (err, newUserAuth){
                                                                if(err){
                                                                    return res.status(200).json({success:false, msg: 'Failed to Save'})
                                                                }
                                                                else {
                                                                    Index.findOneAndUpdate(
                                                                        { key: "key" },
                                                                        { $inc: { "index": 1, }, },
                                                                        { new: true },
                                                                        (err, index) => {
                                                                            if (err) return res.status(200).send({success: false, msg: "Error while Creating New User"});
                                                                            if (index){
                                                                                var newUser = User({
                                                                                    email: req.body.email,
                                                                                    username: req.body.username,
                                                                                    displayname: req.body.displayname,
                                                                                    cnic: req.body.cnic,
                                                                                    address: req.body.address,
                                                                                    contactno: req.body.contactno,
                                                                                    userId: index['index'],
                                                                                    profilepicname: req.body.profilepicname,
                                                                                    type: req.body.type
                                                                                });
                                                                                newUser.save(function (err, newUser){
                                                                                    if(err){
                                                                                        return res.status(200).json({success:false, msg: 'Failed to Save'})
                                                                                    }
                                                                                    else {
                                                                                        if(req.body.type == "Employee"){
                                                                                            var newEmployeeActiveCases = EmployeeActiveCases({
                                                                                                email: req.body.email,
                                                                                                activeCases: 0
                                                                                            });
                                                                                            newEmployeeActiveCases.save(function (err, newUser){
                                                                                                if(err){
                                                                                                    return res.status(200).json({success:false, msg: 'Failed to Save'})
                                                                                                }
                                                                                                else {
                                                                                                    
                                                                                                    return res.status(200).json({success:true, msg: 'Registration Successfull'})
                                                                                                }
                                                                                            })
                                                                                        }
                                                                                        else{
                                                                                            return res.status(200).json({success:true, msg: 'Registration Successfull'})
                                                                                        }
                                                                                    }
                                                                                })
                                                                            } 
                                                                            else{
                                                                                return res.status(200).json({success:false, msg: 'Failed to Save'})
                                                                            }
                                                                        }
                                                                    );
                                                                }
                                                            })
                                                        }
                                                    })
                                                }
                                            })
                                        }
                                        else{
                                            return res.status(200).send({success: false, msg: "Error"});
                                        }
                                    } );
                            }
                            else{
                                return res.status(200).send({success: false, msg: "Token MisMatch"})
                            }
                        }
                        else {
                            return res.status(200).send({success: false, msg: "no Token assigned to the Email address"})
                        }
                    })
                }
            }

            else{
                UserAuth.findOne({
                    email: req.body.email
                }, function (err, user) {
                    if(err) return res.status(200).send({success: false, msg: "Error"});
                    if(user) {
                        return res.status(200).send({success: false, msg: "Email Already exists, Change Email Address"})
                    }
                    else {
                        User.findOne({
                            username: req.body.username
                        }, function (err, user) {
                            if(err) return res.status(200).send({success: false, msg: "Error"});
                            if(user) {
                                return res.status(200).send({success: false, msg: "Username Already exists, Change Username"})
                            }
                            else {
                                var newUserAuth = UserAuth({
                                    email: req.body.email,
                                    password: req.body.password
                                });
                                newUserAuth.save(function (err, newUserAuth){
                                    if(err){
                                        return res.status(200).json({success:false, msg: 'Failed to Save'})
                                    }
                                    else {
                                        Index.findOneAndUpdate(
                                            { key: "key" },
                                            { $inc: { "index": 1, }, },
                                            { new: true },
                                            (err, index) => {
                                                if (err) return res.status(200).send({success: false, msg: "Error while Creating New User"});
                                                if (index){
                                                    var newUser = User({
                                                        email: req.body.email,
                                                        username: req.body.username,
                                                        displayname: req.body.displayname,
                                                        cnic: req.body.cnic,
                                                        address: req.body.address,
                                                        contactno: req.body.contactno,
                                                        userId: index['index'],
                                                        profilepicname: req.body.profilepicname,
                                                        type: req.body.type
                                                    });
                                                    newUser.save(function (err, newUser){
                                                        if(err){
                                                            return res.status(200).json({success:false, msg: 'Failed to Save'})
                                                        }
                                                        else {
                                                            return res.status(200).json({success:true, msg: 'Registration Successfully'})
                                                        }
                                                    })
                                                } 
                                                else{
                                                    return res.status(200).json({success:false, msg: 'Failed to Save'})
                                                }
                                            }
                                        );
                                    }
                                })
                            }
                        })
                    }
                })
            }
            
        }
    },


    authenticate: function(req, res) {
        if((!req.body.email) || (!req.body.password)){
            return res.status(200).json({success:false, msg: 'Enter all Fields'})
        }
        else{
            UserAuth.findOne({
                email: req.body.email
            }, function (err, user) {
                if(err) return res.status(200).send({success: false, msg: "Error"});
                if(!user) {
                    return res.status(200).send({success: false, msg: "Authentication Failed, User not Found"})
                }
                else {
                    user.comparePassword(req.body.password, function (err, isMatch){
                        if(isMatch && !err){
                            User.findOne({
                                email: req.body.email
                            }, function (err, user2) {
                                if(err) return res.status(200).send({success: false, msg: "Error"})
                                if(user2) {
                                    // var token = jwt.encode(user, config.secret)
                                    return res.status(200).json({success:true, msg: "Login Successful.", user: user2})
                                }
                                else {
                                    return res.status(200).send({success: false, msg: "User doesn't exists."})
                                }
                            })
                        }
                        else {
                            return res.status(200).send({success: false, msg: "Authentication Failed, Incorrect Password"})
                        }
                    })
                }
            })
        }
    },


    getUserInfo:  function (req, res) {
        if(req.headers.authorization && req.headers.authorization.split(' ')[0] === 'Bearer') {
            var token = req.headers.authorization.split(' ')[1]
            var decodetoken = jwt.decode(token, config.secret)
            return res.status(200).json({success: true, msg: 'Hello '+decodetoken.email})
        }
        else{
            return res.send({success: false, msg: "No Headers"})
        }
    },


    initializeIndex: function (req,res) {
        Index.findOne({
            key: "key"
        }, function (err, index) {
            if(err) return res.status(200).send({success: false, msg: "Error"});
            if(index) {
                 res.status(200).send({success: false, msg: "index is already initialized"})
            }
            else {
                var newIndex = Index({
                    key: "key",
                    index: 0
                });
                newIndex.save(function (err, newindex){
                    if(err){
                        return res.status(200).json({success:false, msg: 'Failed to Initialize index'})
                    }
                    else if (newindex) {                                    
                        Index.findOne({
                            key: "packageRegisteration"
                        }, function (err, index) {
                            if(err) return res.status(200).send({success: false, msg: "Error"});
                            if(index) {
                                return res.status(200).send({success: false, msg: "packageRegisteration is already initialized"})
                            }
                            else {
                                var newIndex = Index({
                                    key: "packageRegisteration",
                                    index: 0
                                });
                                newIndex.save(function (err, newindex){
                                    if(err){
                                        return res.status(200).json({success:false, msg: 'Failed to Initialize packageRegisteration'})
                                    }
                                    else if (newindex) {                                    
                                        Index.findOne({
                                            key: "packageRegistered"
                                        }, function (err, index) {
                                            if(err) return res.status(200).send({success: false, msg: "Error"});
                                            if(index) {
                                                return res.status(200).send({success: false, msg: "packageRegistered is already initialized"})
                                            }
                                            else {
                                                var newIndex = Index({
                                                    key: "packageRegistered",
                                                    index: 0
                                                });
                                                newIndex.save(function (err, newindex){
                                                    if(err){
                                                        return res.status(200).json({success:false, msg: 'Failed to Initialize packageRegistered'})
                                                    }
                                                    else if (newindex) {                                    
                                                        return res.status(200).json({success:true, msg: 'Successfully saved'})
                                                    }
                                                    else{
                                                        return res.status(200).json({success:false, msg: 'Failed to Initialize packageRegistered'})
                                                    }
                                                })
                                            }
                                        })
                                    }
                                    else{
                                        return res.status(200).json({success:false, msg: 'Failed to Initialize packageRegisteration'})
                                    }
                                })
                            }
                        })
                    }
                    else{
                        return res.status(200).json({success:false, msg: 'Failed to Initialize index'})
                    }
                })
            }
        })
        
        
    },

    
    updateattribute: function(req, res) {
        if ((!req.body.email) || (!req.body.attributename) || (!req.body.newvalue)){
            return res.status(200).json({success:false, msg: 'Enter all Fields'})
        }
        if(req.body.attributename == 'displayname' || req.body.attributename == 'cnic' || req.body.attributename == 'contactno' || req.body.attributename == 'address' || req.body.attributename == 'profilepicname')
        {
            var attributename = req.body.attributename;
            var newvalue = req.body.newvalue;
            User.findOneAndUpdate(
                { email: req.body.email },
                { $set: { [attributename] : newvalue,} },
                (err, index) => {
                    if (err) return res.status(200).send({success: false, msg: "Error"});
                    if (index){
                        return res.status(200).send({success: true, msg: "attribute updated"})
                    } 
                    else{
                        return res.status(200).json({success:false, msg: 'User not found'})
                    }
                }
            );
        }
        else if(req.body.attributename == 'password'){
            var pass = req.body.newvalue;
            bcrypt.genSalt(10, function(err, salt){
                if(err){
                    return res.status(200).send({success: false, msg: "Error"});
                }
                bcrypt.hash(pass, salt, function (err, hash){
                    if(err){
                        return res.status(200).send({success: false, msg: "Error"});
                    }
                    pass = hash;
                    UserAuth.findOneAndUpdate(
                        { email: req.body.email },
                        { $set: { password : pass,} },
                        (err, index) => {
                            if (err) return res.status(200).send({success: false, msg: "Error"});
                            if (index){
                                return res.status(200).send({success: true, msg: "attribute updated"})
                            } 
                            else{
                                return res.status(200).json({success:false, msg: 'User not found'})
                            }
                        }
                    );
                })
            })
        }
        else if(req.body.attributename == 'email'){

            UserAuth.findOne({
                email: req.body.newvalue
            }, function (err, user) {
                if(err) return res.status(200).send({success: false, msg: "Error"});
                if(user) {
                    return res.status(200).send({success: false, msg: "Email Already exists, Change Email Address"})
                }
                else {
                    UserAuth.findOneAndUpdate(
                        { email: req.body.email },
                        { $set: { email : req.body.newvalue,} },
                        (err, index) => {
                            if (err) return res.status(200).send({success: false, msg: "Error"});
                            if (index){
                                User.findOneAndUpdate(
                                    { email: req.body.email },
                                    { $set: { email : req.body.newvalue,} },
                                    (err, index) => {
                                        if (err) return res.status(200).send({success: false, msg: "Error"});
                                        if (index){
                                            return res.status(200).send({success: true, msg: "attribute updated"})
                                        } 
                                        else{
                                            return res.status(200).json({success:false, msg: 'User not found'})
                                        }
                                    }
                                );
                            } 
                            else{
                                return res.status(200).json({success:false, msg: 'User not found'})
                            }
                        }
                    );
                }
            })


            
        }
        else{
            return res.status(200).json({success:false, msg: 'Illegal attribute name'})
        }
    },


    generateRegistrationToken: function(req, res) {
        if((!req.body.email)){
            return res.status(200).json({success:false, msg: 'Enter all Fields'})
        }
        else{
            registrationToken.findOne({
                email: req.body.email
            }, function (err, user) {
                if(err) return res.status(200).send({success: false, msg: "Error"});
                if(user) {
                    var result = '';
                    var characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
                    var charactersLength = characters.length;
                    for ( var i = 0; i < 20; i++ ) {
                        result += characters.charAt(Math.floor(Math.random() *  charactersLength));
                    }
                    var registerationToken = result;
                    registrationToken.findOneAndUpdate(
                        { email: req.body.email },
                        { $set: { registrationtoken: registerationToken,} },
                        (err, index) => {
                            if (err) return res.status(200).send({success: false, msg: "Error"});
                            if (index){
                                return res.status(200).json({success:true, msg: 'Token Generated another time', registerationtoken:registerationToken})
                            } 
                            else{
                                return res.status(200).json({success:false, msg: 'Email not found'})
                            }
                        }
                    );
                }
                else {
                    var result = '';
                    var characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
                    var charactersLength = characters.length;
                    for ( var i = 0; i < 20; i++ ) {
                        result += characters.charAt(Math.floor(Math.random() *  charactersLength));
                    }
                    var registerationToken = result;
                    var newRegistrationToken = registrationToken({
                        email: req.body.email,
                        registrationtoken: registerationToken
                    });
                    newRegistrationToken.save(function (err, newRegistrationToken){
                        if(err){
                            return res.status(200).json({success:false, msg: 'Failed to Generate Token'})
                        }
                        if (newRegistrationToken) {
                            Index.findOneAndUpdate(
                                { key: "key" },
                                { $inc: { "index": 1, }, },
                                { new: true },
                                (err, index) => {
                                    if (err) return res.status(200).send({success: false, msg: "Error while Creating New Token"});
                                    if (index){
                                        return res.status(200).json({success:true, msg: 'Token Generated', registerationtoken:registerationToken})
                                    } 
                                    else{
                                        return res.status(200).send({success: false, msg: "Error while Creating New Token"});
                                    }
                                }
                            );
                        }
                    })
                }
            })
        }
    },


    addNewPackage: function (req,res) {
        if( (!req.body.packageId) || (!req.body.packagename) || (!req.body.description) || (!req.body.picurl) ){
            return res.status(200).json({success:false, msg: 'Enter all Fields'})
        }
        else{
            InsuancePackage.findOne({
                packageId: req.body.packageId
            }, function (err, package) {
                if(err) return res.status(200).send({success: false, msg: "Error"});
                if(package) {
                    return res.status(200).send({success: false, msg: "Package Id Already exists, Package Id"})
                }
                else {
                    var newInsuancePackage = InsuancePackage({
                        packageId: req.body.packageId,
                        packagename: req.body.packagename,
                        description: req.body.description,
                        picurl: req.body.picurl,
                    });
                    newInsuancePackage.save(function (err, newInsuancePackage){
                        if(err){
                            return res.status(200).json({success:false, msg: 'Failed to Save'})
                        }
                        else {
                            return res.status(200).json({success:true, msg: 'Successfully saved'})
                        }
                    })
                }
            })
        }
    },


    updatePackage: function(req, res) {
        if ((!req.body.packageId) || (!req.body.attributename) || (!req.body.newvalue)){
            return res.status(200).json({success:false, msg: 'Enter all Fields'})
        }
        if(req.body.attributename == 'packagename' || req.body.attributename == 'description' || req.body.attributename == 'picurl' )
        {
            var attributename = req.body.attributename;
            var newvalue = req.body.newvalue;
            InsuancePackage.findOneAndUpdate(
                { packageId: req.body.packageId },
                { $set: { [attributename] : newvalue,} },
                (err, index) => {
                    if (err) return res.status(200).send({success: false, msg: "Error"});
                    if (index){
                        return res.status(200).send({success: true, msg: "package attribute updated"})
                    } 
                    else{
                        return res.status(200).json({success:false, msg: 'Package not found'})
                    }
                }
            );
        }
        else{
            return res.status(200).json({success:false, msg: 'Illegal attribute name'})
        }
    },

    deletePackage: function(req, res) {
        if ((!req.body.packageId)){
            return res.status(200).json({success:false, msg: 'Enter all Fields'})
        }
        else
        {
            InsuancePackage.deleteOne( { 
                "email" : req.body.email },
                function (err,token){
                    if(err) return res.status(200).send({success: false, msg: "Error"});
                    if(token['deletedCount'] == 1) {
                        return res.status(200).send({success: true, msg: "package deleted"})
                    }
                    else{
                        return res.status(200).send({success: false, msg: "Package not found"});
                    }
            } );
        }
    },

    deleteUser: function(req, res) {
        if ((!req.body.email)){
            return res.status(200).json({success:false, msg: 'Enter all Fields'})
        }
        else
        {
            UserAuth.deleteOne( { 
                "email" : req.body.email },
                function (err,token){
                    if(err) return res.status(200).send({success: false, msg: "Error"});
                    if(token['deletedCount'] == 1) {
                        User.deleteOne( { 
                            "email" : req.body.email },
                            function (err,token){
                                if(err) return res.status(200).send({success: false, msg: "Error"});
                                if(token['deletedCount'] == 1) {
                                    return res.status(200).send({success: true, msg: "Account deleted"})
                                }
                                else{
                                    return res.status(200).send({success: false, msg: "Account not found"});
                                }
                        } );
                    }
                    else{
                        return res.status(200).send({success: false, msg: "Account not found"});
                    }
            } );
        }
    },

    allPackages: function(req, res) {
        InsuancePackage.find({}, function(err, packages) {
            if(err) return res.status(200).send({success: false, msg: "Error"});

            var packageMap = {};
        
            packages.forEach(function(packages) {
              packageMap[packages.packageId] = packages;
            });
            return res.status(200).send({success: true, msg: "Data Recieved", packages:packageMap});
        });
    },

    allUsers: function(req, res) {
        User.find({}, function(err, users) {
            if(err) return res.status(200).send({success: false, msg: "Error"});

            var UserMap = {};
        
            users.forEach(function(users) {
                UserMap[users.userId] = users;
            });
            return res.status(200).send({success: true, msg: "Data Recieved", users:UserMap});
        });
    },

    allEmployees: function(req, res) {
        User.find({type: "Employee"}, function(err, users) {
            if(err) return res.status(200).send({success: false, msg: "Error"});

            var UserMap = {};
        
            users.forEach(function(users) {
                UserMap[users.userId] = users;
            });
            return res.status(200).send({success: true, msg: "Data Recieved", users:UserMap});
        });
    },

    addinProcessPackage: function (req,res)  {
        if( (!req.body.packageId) || (!req.body.userId)){
            return res.status(200).json({success:false, msg: 'Enter all Fields'})
        }
        else{
            InsuancePackage.findOne({
                packageId: req.body.packageId
            }, function (err, package) {
                if(err) return res.status(200).send({success: false, msg: "Error"});
                if(package) {
                    User.findOne({
                        userId: req.body.userId
                    }, async function (err, user) {
                        if(err) return res.status(200).send({success: false, msg: "Error"});
                        if(user) {
                            EmployeeActiveCases.find().sort({activeCases: 1}).limit(1).findOne({}, function (err, lowemployee){

                                if(err) return res.status(200).send({success: false, msg: "Error"});
                                if(lowemployee) {
                                    User.findOne({
                                        email: lowemployee['email']
                                    }, function (err, employee) {
                                        if(err) return res.status(200).send({success: false, msg: "Error"});
        
                                        if(employee) {
                                            Index.findOneAndUpdate(
                                                { key: "packageRegisteration" },
                                                { $inc: { "index": 1, }, },
                                                { new: true },
                                                (err, index) => {
                                                    if (err) return res.status(200).send({success: false, msg: "Error while Creating New User"});
                                                    if (index){
                                                        var newinprocessPackage = InProcessInsuancePackage({
                                                            inprocessPackageId: index["index"],
                                                            userId: user['userId'],
                                                            employeeId: employee['userId'],
                                                            userName: user['username'],
                                                            employeeName: employee['username'],
                                                            packageId: package['packageId'],
                                                            packagename: package['packagename'],
                                                            description: package['description'],
                                                            picurl: package['picurl'],
                                                        });
                                                        newinprocessPackage.save(function (err, newinprocessPackage){
                                                            if(err){
                                                                return res.status(200).json({success:false, msg: 'Failed to Save'})
                                                            }
                                                            else {
                                                                
                                                                EmployeeActiveCases.findOneAndUpdate(
                                                                    { email: employee['email'] },
                                                                    { $inc: { "activeCases": 1, }, },
                                                                    { new: true },
                                                                    (err, index) => {
                                                                        if (err) return res.status(200).send({success: false, msg: "Error while Creating New User"});
                                                                        if (index){
                                                                            return res.status(200).json({success:true, msg: 'Package Registered Successfully'})
                                                                        } 
                                                                        else{
                                                                            return res.status(200).json({success:false, msg: 'Failed to Save'})
                                                                        }
                                                                    }
                                                                );
                                                            }
                                                        })
                                                    } 
                                                    else{
                                                        return res.status(200).json({success:false, msg: 'Failed to Save'})
                                                    }
                                                }
                                            );
        
                                        }
        
                                        else{
                                            return res.status(200).json({success:false, msg: 'Error finding Employee with minimum Active Cases'})
                                        }
                                    })
        
                                }
                                else {
                                    return res.status(200).json({success:false, msg: 'Error finding Employee with minimum Active Cases'})
                                }
                                
                            });
                            
                        }
                        else {
                            return res.status(200).send({success: false, msg: "User Not Found"})
                        }
                    })
                }
                else {
                    return res.status(200).json({success:false, msg: 'Package Not found'})
                }
            })

            
        }
    },

    addNewPackageRegisterationComplete: function (req,res) {
        if( (!req.body.inprocessPackageId)){
            return res.status(200).json({success:false, msg: 'Enter all Fields'})
        }
        else{
            InProcessInsuancePackage.findOne({
                inprocessPackageId: req.body.inprocessPackageId
            }, function (err, package) {
                if(err) return res.status(200).send({success: false, msg: "Error"});
                if(package) {
                    Index.findOneAndUpdate(
                        { key: "packageRegistered" },
                        { $inc: { "index": 1, }, },
                        { new: true },
                        (err, index) => {
                            if (err) return res.status(200).send({success: false, msg: "Error"});
                            if (index){
                                var newRegisteredInsuancePackage = RegisteredInsuancePackage({
                                    registeredPackageId: index['index'],
                                    userId: package['userId'],
                                    packageId: package['packageId'],
                                    packagename: package['packagename'],
                                    description: package['description'],
                                    picurl: package['picurl'],
                                });
                                newRegisteredInsuancePackage.save(function (err, newInsuancePackage){
                                    if(err){
                                        return res.status(200).json({success:false, msg: 'Failed to Save'})
                                    }
                                    else {
                                        User.findOne({
                                            userId: package['employeeId']
                                        }, function (err, employee) {
                                            if(err) return res.status(200).send({success: false, msg: "Error"});
            
                                            if(employee) {
                                                EmployeeActiveCases.findOneAndUpdate(
                                                    { email: employee['email'] },
                                                    { $inc: { "activeCases": -1, }, },
                                                    { new: true },
                                                    (err, index) => {
                                                        if (err) return res.status(200).send({success: false, msg: "Error while Creating New User"});
                                                        if (index){
                                                            InProcessInsuancePackage.deleteOne( 
                                                                { inprocessPackageId: req.body.inprocessPackageId },
                                                                function (err,token){
                                                                    if(err) return res.status(200).send({success: false, msg: "Error"});
                                                                    if(token['deletedCount'] == 1) {
                                                                        return res.status(200).json({success:true, msg: 'Package Registered Successfully'})
                                                                    }
                                                                    else{
                                                                        return res.status(200).send({success: false, msg: "Package not found"});
                                                                    }
                                                            } );
                                                        } 
                                                        else{
                                                            return res.status(200).json({success:false, msg: 'Failed to Save'})
                                                        }
                                                    }
                                                );
                                            }
                                            else{
                                                return res.status(200).json({success:false, msg: 'Failed to Save'})
                                            }
                                        });
                                    }
                                })
                            }
                            else{

                            }
                        })
                }
                else {
                    return res.status(200).send({success: false, msg: "Package Process not found"})
                }
            })
        }
    },

    allInProcessPackages: function(req, res) {
        if( (!req.body.userId) || (!req.body.type) ){
            return res.status(200).json({success:false, msg: 'Enter all Fields'})
        }

        var Id = "";
        if(req.body.type == "Employee"){
            Id = "employeeId"
        }
        else{
            Id = "userId"
        }

        InProcessInsuancePackage.find({[Id]: req.body.userId}, function(err, packages) {
            if(err) return res.status(200).send({success: false, msg: "Error"});

            var packageMap = {};
        
            packages.forEach(function(packages) {
              packageMap[packages.inprocessPackageId] = packages;
            });
            return res.status(200).send({success: true, msg: "Data Recieved", packages:packageMap});
        });
    },

    allRegisteredPackages: function(req, res) {
        if( (!req.body.userId) ){
            return res.status(200).json({success:false, msg: 'Enter all Fields'})
        }

        RegisteredInsuancePackage.find({Id: req.body.userId}, function(err, packages) {
            if(err) return res.status(200).send({success: false, msg: "Error"});

            var packageMap = {};
        
            packages.forEach(function(packages) {
              packageMap[packages.registeredPackageId] = packages;
            });
            return res.status(200).send({success: true, msg: "Data Recieved", packages:packageMap});
        });
    },

    fileUrlUpdate: function(req, res) {
        if ((!req.body.userId) || ((!req.body.fileUrl)&&req.body.fileUrl!="") ){
            console.log(req.body.userId+":"+req.body.fileUrl)
            return res.status(200).json({success:false, msg: 'Enter all Fields'})
        }
        UserFile.findOneAndUpdate(
            { userId: req.body.userId },
            { $set: { fileUrl : req.body.fileUrl,} },
            (err, index) => {
                if (err) return res.status(200).send({success: false, msg: "Error"});
                if (index){
                    return res.status(200).send({success: true, msg: "File Url updated", fileUrl:index['fileUrl']})
                } 
                else{
                    var newUserFile = UserFile({
                        userId: req.body.userId,
                        fileUrl: req.body.fileUrl,
                    });
                    newUserFile.save(function (err, newUserFile){
                        if(err){
                            return res.status(200).json({success:false, msg: 'Failed to Save'})
                        }
                        else {
                            return res.status(200).send({success: true, msg: "File Url updated", fileUrl:newUserFile['fileUrl']})
                        }
                    });
                }
            }
        );
    },

    getfileUrl: function(req, res) {
        if ((!req.body.userId) ){
            return res.status(200).json({success:false, msg: 'Enter all Fields'})
        }
        UserFile.findOne(
            { userId: req.body.userId },
            (err, index) => {
                if (err) return res.status(200).send({success: false, msg: "Error"});
                if (index){
                    return res.status(200).send({success: true, msg: "Successfull", fileUrl:index["fileUrl"]})
                } 
                else{
                    return res.status(200).json({success:false, msg: 'User Url not found'})
                }
            }
        );
    },


}

module.exports = functions