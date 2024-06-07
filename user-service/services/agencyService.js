const asyncHandler = require("express-async-handler");

const User = require("../models/User");
const ApiError = require("../utils/apiError");
const sendEmail = require("../utils/email");



/*
@desc   Create Agency
@route  POST /agency
@acess  Private
*/
exports.createAgency = asyncHandler(async (req, res, next) => {
    // 1-) Check if email is already registered
    const checkEmail = await User.exists({ email: req.body.email });
    if (checkEmail) return next(new ApiError("Email already in use", 401));

    // 2-) Check if phone number is already registered
    const checkPhone = await User.exists({ phoneNumber: req.body.phoneNumber });
    if (checkPhone) return next(new ApiError("Phone number already in use", 401));

    // 3-) Create a new user
    const user = await User.create({
        name: req.body.name,
        email: req.body.email,
        password: req.body.password,
        phoneNumber: req.body.phoneNumber,
        wilaya: req.body.wilaya,
        role: "agency",
    });


    // 4-) Send welcome email
    await sendEmail(
        {
            to: user.email,
            subject: `${user.name}, welcome to Buy it!`,
            template: `welcome`,
        },
        {
            name: user.name,
        }
    );

    res.status(201).json({data: user});
});

