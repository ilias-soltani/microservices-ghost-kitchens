const express = require("express");

const {
  signup,
  login,
  signupChef,
  uploadChefImages,
    getUser
} = require("../services/authService");
const {
  signupValidation,
  loginValidation,
  signupChefValidation,
    getUserValidation
} = require("../validations/authValidation");

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

module.exports = router;
