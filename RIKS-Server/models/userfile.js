var mongoose = require('mongoose')
var Schema = mongoose.Schema;

var userFileSchema= new Schema({
    userId: {
        type: Number,
        required:true
    },
    fileUrl: {
        type: String,
        default:"",
    },
})

module.exports = mongoose.model('UserFile', userFileSchema)