const asyncHandler = require("express-async-handler");

const User = require("../models/User");
const Restaurant = require("../models/Restaurant");
const ApiError = require("../utils/apiError");

/*
@desc   Chef payment status
@route  POST /chef/payment
@acess  Private:Chef
*/
exports.payment = asyncHandler(async (req, res) => {
    req.user.status = "pending"

    await req.user.save();

    res.status(200).json({data: req.user});
});

/*
@desc   Get Chef Restaurant
@route  POST /chef/restaurant
@acess  Private:Chef
*/
exports.getChefRestaurant = asyncHandler(async (req, res) => {

    const restaurant = await Restaurant.findOne({user: req.user._id});

    res.status(200).json({data: restaurant})
});

/*
@desc   Get Chef Restaurant By Id
@route  POST /chef/restaurant/:id
@acess  Private:Admin
*/
exports.getChefRestaurantByIdChef = asyncHandler(async (req, res, next) => {

    const restaurant = await Restaurant.findOne({user: req.params.id});

    if (!restaurant) return next(new ApiError("Restaurant not found!", 404));

    res.status(200).json({data: restaurant})
});