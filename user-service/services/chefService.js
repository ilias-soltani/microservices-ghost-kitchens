const asyncHandler = require("express-async-handler");

const User = require("../models/User");
const Restaurant = require("../models/Restaurant");
const ApiError = require("../utils/apiError");

/*
@desc   Chef payment status
@route  POST /chef/payment
@acess  Private:Admin
*/
exports.payment = asyncHandler(async (req, res) => {
    req.user.status = "pending"

    await req.user.save();

    res.status(200).json({data: req.user});
});