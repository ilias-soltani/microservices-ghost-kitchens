const asyncHandler = require("express-async-handler");
const jwt = require("jsonwebtoken");

const User = require("../models/User");
const ApiError = require("../utils/apiError.js");

exports.auth = asyncHandler(async (req, res, next) => {
    // 1-) Check if token exists
    let token;
    if (
        req.headers.authorization &&
        req.headers.authorization.startsWith("Bearer ")
    )
        token = req.headers.authorization.split(" ")[1];
    if (!token) return next(new ApiError("Please login or sign up", 401));

    // 2-) Check if token is not valid or if token is expired
    const decodedToken = jwt.verify(token, process.env.JWT_SECRET_KEY);

    // 3-) Check if user exists
    const user = await User.findById(decodedToken.userId);
    if (!user)
        return next(new ApiError("Token invalid: User dose not exists", 401));

    // 4-) Check if user email is verified
    if (!user.verified)
        return next(new ApiError("Token invalid: User is not verified", 401));

    // 5-) Check if user password has been changed
    if (user.passwordChangedAt) {
        const passwordTimestamp = parseInt(
            user.passwordChangedAt.getTime() / 1000,
            10
        );

        if (passwordTimestamp > decodedToken.iat)
            return next(
                new ApiError("Token invalid: User password has been changed", 401)
            );
    }
    req.user = user;
    next();
});

exports.access = (...roles) =>
    asyncHandler(async (req, res, next) => {
        if (!roles.includes(req.user.role))
            return next(new ApiError("Access denied!", 403));
        next();
    });
