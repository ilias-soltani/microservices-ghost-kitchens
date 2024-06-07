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
    let query = {}
    if (req.query.wilaya) {
        const regex = new RegExp(req.query.wilaya, "i");
        query = {
            "address.wilaya": {
                $regex: regex
            }
        };
        delete req.query.wilaya;
    }
    const apiFeatures = new ApiFeatures(Restaurant.find(query), req.query)
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


/*
@desc   Get Restaurant By ID
@route  GET /restaurants/:id
@acess  Public
*/
exports.getRestaurant = asyncHandler(async (req, res, next) => {
    const id = req.params.id;

    const restaurant = await Restaurant.findById(id);

    if(!restaurant) return next(new ApiError("Restaurant not found", 404));



    res.status(200).json({ data: restaurant });
});
