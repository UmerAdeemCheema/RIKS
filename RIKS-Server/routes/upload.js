// const express = require('express');
// const path = require("path");
// const router = express.Router();
// const multer = require('multer')
// var User = require('../models/user')

// const profilepicstorage = multer.diskStorage({
//     destination: (req, file, cb) => {
//       cb(null, "./uploads/profilepics");
//     },
//     filename: (req, file, cb) => {
//       cb(null, req.headers.username + ".jpg");
//     },
// });

// const fileFilter = (req, file, cb) => {
//     if (file.mimetype == "image/jpeg" || file.mimetype == "image/png") {
//       cb(null, true);
//     } else {
//       cb(null, false);
//     }
// };
  
// const upload = multer({
//     storage: profilepicstorage,
//     limits: {
//       fileSize: 1024 * 1024 * 10,
//     },
//     // fileFilter: fileFilter,
// });

// //adding and update profile image
// router
//   .route("/profilepic")
//   .patch( upload.single("profilepic"), (req, res) => {
    
//     User.findOneAndUpdate(
//       { username: req.headers.username },
//       {
//         $set: {
//             profilepicname: req.file.path,
//         },
//       },
//       { new: true },
//       (err, profile) => {
//         if (err) return res.status(500).send({success: false, msg: "Error while Uploading Profile Pic"});
//         const response = {
//           message: "image added successfully updated",
//           data: profile,
//         };
//         return res.status(200).send({success: true, msg: "Profile Pic successfully updated", data: profile,});
//       }
//     );
//   });

//   module.exports = router;