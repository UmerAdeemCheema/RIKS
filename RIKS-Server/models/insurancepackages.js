var mongoose = require('mongoose')
var Schema = mongoose.Schema;

var insuancePackageSchema= new Schema({
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

module.exports = mongoose.model('InsuancePackage', insuancePackageSchema)