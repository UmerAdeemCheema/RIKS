var mongoose = require('mongoose')
var Schema = mongoose.Schema;

var inprocessinsuancePackageSchema= new Schema({
    inprocessPackageId: {
        type: String,
        required:true
    },
    userId: {
        type: String,
        required:true
    },
    employeeId: {
        type: String,
        required:true
    },
    userName: {
        type: String,
        required:true
    },
    employeeName: {
        type: String,
        required:true
    },
    packageId: {
        type: String,
        required:true
    },
    packagename: {
        type: String,
        required:true
    },
    description: {
        type: String,
        required:true
    },
    picurl: {
        type: String,
        default:"",
    },
})

module.exports = mongoose.model('InProcessInsuancePackage', inprocessinsuancePackageSchema)