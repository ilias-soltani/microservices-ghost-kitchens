const express = require("express");

const {
  signup,
  login,
  signupChef,
  uploadChefImages,
    getUser,
    getMe
} = require("../services/authService");
const {
  signupValidation,
  loginValidation,
  signupChefValidation,
    getUserValidation
} = require("../validations/authValidation");
const {auth, access} = require("../middlewares/authMiddleware");


const router = express.Router();

router.post("/signup", signupValidation, signup);

router.post(
  "/signup/chef",
    uploadChefImages,
  signupChefValidation,
  signupChef
);

router.get("", (req, res) => {
  res.send("Hello World!");
})

router.post("/login/:role", loginValidation, login);

router.get("/user/:id", getUserValidation, getUser);

router.get("/profile", auth, getMe);

module.exports = router;
