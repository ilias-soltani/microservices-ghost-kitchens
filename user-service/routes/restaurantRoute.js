const express = require("express");

const {
    getRestaurants,
    getRestaurant
} = require("../services/restaurantService")

const {
   getUserValidation
} = require("../validations/authValidation")



const router = express.Router();


router.get("/", getRestaurants);

router.get("/:id", getUserValidation, getRestaurant);


module.exports = router;