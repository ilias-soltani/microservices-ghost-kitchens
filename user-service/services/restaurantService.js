const asyncHandler = require("express-async-handler");

const User = require("../models/User");
const Restaurant = require("../models/Restaurant");
const ApiError = require("../utils/apiError");
const ApiFeatures = require("../utils/apiFeatures");


/*
@desc   Get restaurants
@route  GET /restaurants
@acess  Public
*/
exports.getRestaurants = asyncHandler(async (req, res) => {
    const apiFeatures = new ApiFeatures(Restaurant.find(), req.query)
        .filter()
        .search()
        .sort()
        .fileds();
    apiFeatures.paginate(
        await apiFeatures.mongooseQuery.clone().countDocuments()
    );

    const { mongooseQuery, pagination } = apiFeatures;

    const restaurants = await mongooseQuery;

    res.status(200).json({ pagination, data: restaurants });
});