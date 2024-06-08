const express = require("express");

const {
    payment,
    getChefRestaurant,
    getChefRestaurantByIdChef
} = require("../services/chefService")

const {
    getUserValidation
} = require("../validations/authValidation")

const {
    auth,
    access
} = require("../middlewares/authMiddleware")

const router = express.Router();

router.post("/payment", auth, access("chef"), payment);

router.get("/restaurant", auth, access("chef"), getChefRestaurant);

router.get("/restaurant/:id", auth, access("admin"), getUserValidation, getChefRestaurantByIdChef);

module.exports = router;