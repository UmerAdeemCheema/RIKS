var mongoose = require('mongoose')
var Schema = mongoose.Schema;
var bcrypt = require('bcrypt')

var userauthSchema= new Schema({
    email: {
        type: String,
        required:true
    },
    password: {
        type: String,
        required:true
    }
})

userauthSchema.pre('save', function(next){
    var userauth = this;
    if(this.isModified('password') || this.isNew){
        bcrypt.genSalt(10, function(err, salt){
            if(err){
                return next(err)
            }
            bcrypt.hash(userauth.password, salt, function (err, hash){
                if(err){
                    return next(err)
                }
                userauth.password=hash;
                next()
            })
        })
    }
    else{
        return next()
    }
})

userauthSchema.methods.comparePassword = function (pass, cb) {
    bcrypt.compare(pass, this.password, function(err, isMatch){
        if(err){
            return next(err)
        }
        cb(null, isMatch)
    })
}

module.exports = mongoose.model('UserAuth', userauthSchema)