const asyncHandler = require("express-async-handler");
const jwt = require("jsonwebtoken");

const User = require("../models/User");
const ApiError = require("../utils/apiError");


exports.auth = asyncHandler(async (req, res, next) => {
    // 1-) Check if token exists
    let token;
    if (
        req.headers.authorization &&
        req.headers.authorization.startsWith("Bearer ")
    )
        token = req.headers.authorization.split(" ")[1];
    if (!token) return res.status(401).json({error: "Please login or sign up"});;


    // 2-) Check if token is not valid or if token is expired
    const decodedToken = jwt.verify(token, process.env.JWT_SECRET_KEY);

    // 3-) Check if user exists
    const user = await User.findById(decodedToken.userId);
    if (!user)
        return res.status(401).json({error: "Token invalid: User dose not exists"});


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
            return res.status(401).json({error: "Token invalid: User password has been changed"});

    }

    // 6-) Check user role
    if (!req.body.roles.includes(user.role))
        return res.status(403).json({error: "Access denied!"});


    return res.status(200).json({user});
});

