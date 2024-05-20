const express = require("express");

const {
    getRestaurants
} = require("../services/restaurantService")


const router = express.Router();


router.get("/", getRestaurants);


module.exports = router;