const express = require("express");
const actions = require('../methods/actions')
const router = express.Router();

// router.get('/', (req,res)=>{
//     res.send('Hello World')
// })

//adding a user
//route POST /adduser
router.post('/adduser', actions.addNewUser)
//authenticate a user
//route POST /authenticate
router.post('/authenticate', actions.authenticate)

router.get('/getInfo', actions.getUserInfo)

// router.use('/upload', require('./upload'))
router.get('/initializeIndex', actions.initializeIndex)

router.post('/deleteUser', actions.deleteUser)

router.post('/updateattribute', actions.updateattribute)

router.post('/generateRegistrationToken', actions.generateRegistrationToken)

router.post('/addNewPackage', actions.addNewPackage)

router.post('/updatePackage', actions.updatePackage)

router.post('/deletePackage', actions.deletePackage)

router.get('/allPackages', actions.allPackages)

router.get('/allUsers', actions.allUsers)

router.get('/allEmployees', actions.allEmployees)

router.post('/addinProcessPackage', actions.addinProcessPackage)

router.post('/addNewPackageRegisterationComplete', actions.addNewPackageRegisterationComplete)

router.post('/allInProcessPackages', actions.allInProcessPackages)

router.post('/allRegisteredPackages', actions.allRegisteredPackages)

router.post('/fileUrlUpdate', actions.fileUrlUpdate)

router.post('/getfileUrl', actions.getfileUrl)

module.exports = router;