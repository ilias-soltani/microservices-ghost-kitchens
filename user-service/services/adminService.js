const asyncHandler = require("express-async-handler");

const User = require("../models/User");
const Restaurant = require("../models/Restaurant");
const ApiError = require("../utils/apiError");
const ApiFeatures = require("../utils/apiFeatures")
const sendEmail = require("../utils/email");

/*
@desc   Update chef status
@route  POST /admin/update-chef-status/:id
@acess  Private:Admin
*/
exports.updateChefStatus = asyncHandler(async (req, res, next) =>{
    const chefId =  req.params.id;

    let updatedValue = {verified: req.body.verified}

    if (req.body.verified) {
        let currentDate = new Date();
        let futureDate = new Date();
        futureDate.setDate(currentDate.getDate() + 30); // Add 30 days to the current date
        updatedValue.paymentAt = futureDate;
    }


    const chef = await User.findOneAndUpdate({_id: chefId, role: "chef"},
        updatedValue, {new: true});

    if(!chef) return next(new ApiError("Chef not found!", 404));

    await Restaurant.findOneAndUpdate({user: chef._id}, {verified: req.body.verified});

    if(chef.verified && chef.firstTime) {
        await sendEmail(
            {
                to: chef.email,
                subject: `${chef.name}, welcome to Buy it!`,
                template: `welcome`,
            },
            {
                name: chef.name,
            }
        );
        chef.firstTime = false;
        await chef.save();
    }


    res.status(200).json({data: chef});
});

/*
@desc   Update chef payment status
@route  POST /admin/update-chef-payment-status/:id
@acess  Private:Admin
*/
exports.updateChefPaymnet = asyncHandler(async (req, res, next) => {
    const chefId =  req.params.id;

    let updatedValue = {status: req.body.status}

    if(req.body.status === "valid") {
        let currentDate = new Date();
        let futureDate = new Date();
        futureDate.setDate(currentDate.getDate() + 30); // Add 30 days to the current date
        updatedValue.paymentAt = futureDate;
    }

    const chef = await User.findOneAndUpdate({_id: chefId, role: "chef"},
        updatedValue, {new: true});

    if(!chef) return next(new ApiError("Chef not found!", 404));

    res.status(200).json({data: chef})
})

/*
@desc   Get users
@route  GET /admin/users
@acess  Private:Admin
*/
exports.getUsers = asyncHandler(async (req, res) => {
    if(req.query.paymentAt) {
        for (let key in req.query.paymentAt) {
            req.query.paymentAt[key] = new Date(req.query.paymentAt[key])
        }
    }
    let query = {}
    if (req.query.wilaya) {
        const regex = new RegExp(req.query.wilaya, "i");
        query = {
            wilaya: {
                $regex: regex
            }
        };
        delete req.query.wilaya;
    }
    const apiFeatures = new ApiFeatures(User.find(query), req.query)
        .filter()
        .search()
        .sort()
        .fileds();
    apiFeatures.paginate(
        await apiFeatures.mongooseQuery.clone().countDocuments()
    );

    const { mongooseQuery, pagination } = apiFeatures;

    const users = await mongooseQuery;

    res.status(200).json({ pagination, data: users });
});