const express = require("express");

const {
    payment
} = require("../services/chefService")

const {
    auth,
    access
} = require("../middlewares/authMiddleware")

const router = express.Router();

router.post("/payment", auth, access("chef"), payment);

module.exports = router;