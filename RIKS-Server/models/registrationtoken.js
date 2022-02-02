var mongoose = require('mongoose')
var Schema = mongoose.Schema;

var registrationTokenSchema= new Schema({
    email: {
        type: String,
        required:true
    },
    registrationtoken: {
        type: String,
        required:true
    }
})

module.exports = mongoose.model('registrationToken', registrationTokenSchema)