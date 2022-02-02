var mongoose = require('mongoose')
var Schema = mongoose.Schema;

var indexSchema= new Schema({
    key: {
        type: String,
        default: "key"
    },
    index: {
        type: Number
    }
})

module.exports = mongoose.model('Index', indexSchema)