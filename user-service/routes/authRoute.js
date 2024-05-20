const express = require("express");

const {
  signup,
  login,
  signupChef,
  uploadRestaurantImage,
} = require("../services/authService");
const {
  signupValidation,
  loginValidation,
  signupChefValidation,
} = require("../validations/authValidation");

const router = express.Router();

router.post("/signup", signupValidation, signup);

router.post(
  "/signup/chef",
  uploadRestaurantImage,
  signupChefValidation,
  signupChef
);

router.get("", (req, res) => {
  res.send("Hello World!");
})

router.post("/login/:role", loginValidation, login);

module.exports = router;
