const express = require("express");

const {
    auth
} = require("../services/jwtService")

const {
    accessValidation
} = require("../validations/jwtValidation")

const router = express.Router();

router.post("/verify-token", accessValidation, auth);

module.exports = router;



