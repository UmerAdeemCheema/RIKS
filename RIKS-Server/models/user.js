var mongoose = require('mongoose')
var Schema = mongoose.Schema;
var bcrypt = require('bcrypt')

var userSchema= new Schema({
    email: {
        type: String,
        required:true
    },
    username: {
        type: String,
        required:true
    },
    displayname: {
        type: String,
        required:true
    },
    cnic: {
        type: String,
        required:true
    },
    address: {
        type: String,
        required:true
    },
    contactno: {
        type: String,
        required:true
    },
    type: {
        type: String,
        required:true
    },
    userId: {
        type: Number,
        required:true
    },
    profilepicname: {
        type: String,
        default:"",
    },
})

module.exports = mongoose.model('User', userSchema)