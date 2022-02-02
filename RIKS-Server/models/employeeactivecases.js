var mongoose = require('mongoose')
var Schema = mongoose.Schema;
var bcrypt = require('bcrypt')

var employeeActiveCasesSchema= new Schema({
    email: {
        type: String,
        required:true
    },
    activeCases: {
        type: Number,
        required:true
    },
})

module.exports = mongoose.model('EmployeeActiveCases', employeeActiveCasesSchema)