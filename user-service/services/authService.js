const asyncHandler = require("express-async-handler");
const bcrypt = require("bcryptjs");
const { v4: uuidv4 } = require("uuid");

const User = require("../models/User");
const Restaurant = require("../models/Restaurant");
const ApiError = require("../utils/apiError");
const generateTwtToken = require("../utils/jwt");
const { uploadSingleImage, uploadMixedImages } = require("../middlewares/imagesMiddleware");
const { uploadImage } = require("../firebase/storage");

// Upload image using multer
exports.uploadChefImages = uploadMixedImages([
  {
    name: "logo",
    maxCount: 1,
  },
  {
    name: "photo",
    maxCount: 1,
  }
]);

const generateImageName = (name, type = "logo") => {
  return `${name.toLowerCase().replace(/[\s/]/g, "-")}-${uuidv4()}-${type}.webp`;
};


/*
@desc   Signup
@route  POST /auth/signup
@acess  Public 
*/
exports.signup = asyncHandler(async (req, res, next) => {
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
    role: req.body.role || "client",
  });

  // 3-) Generate jwt token
  const token = generateTwtToken(user._id);

  res.status(201).json({ data: user, token });
});

/*
@desc   Signup chef
@route  POST /auth/signup/chef
@acess  Public 
*/
exports.signupChef = asyncHandler(async (req, res, next) => {
  if (!req.files) return next(new ApiError("Images are required"));
  if (!req.files.logo) return next(new ApiError("Restaurant logo is required"));
  if (!req.files.photo) return next(new ApiError("Card photo is required"));

  // 1-) Check if email is already registered
  const checkEmail = await User.exists({ email: req.body.email });
  if (checkEmail) return next(new ApiError("Email already in use", 401));

  // 2-) Check if phone number is already registered
  const checkPhone = await User.exists({ phoneNumber: req.body.phoneNumber });
  if (checkPhone) return next(new ApiError("Phone number already in use", 401));

  // 3-) Check if restaurant name is already registered
  const checkRestaurant = await Restaurant.exists({
    name: req.body.restaurantName.toLowerCase(),
  });
  if (checkRestaurant)
    return next(new ApiError("Restaurant name already in use", 401));

  // 4-) Create a new user

  req.body.nationalCard.photo = await uploadImage(
      req.files.photo[0].buffer,
      generateImageName(req.body.nationalCard.surname, "chef"),
      "chefs"
  );

  const user = await User.create({
    name: req.body.name,
    email: req.body.email,
    password: req.body.password,
    phoneNumber: req.body.phoneNumber,
    nationalCard: req.body.nationalCard,
    verified: false,
    role: "chef",
  });

  // 5-) Create restaurant
  req.body.logo = await uploadImage(
    req.files.logo[0].buffer,
    generateImageName(req.body.restaurantName)
  );

  await Restaurant.create({
    name: req.body.restaurantName,
    logo: req.body.logo,
    ccp: req.body.ccp,
    address: req.body.address,
    user: user._id.toString(),
  });

  // 6-) Generate jwt token
  const token = generateTwtToken(user._id);

  res.status(201).json({ data: user, token });
});

/*
@desc   Login
@route  POST /auth/login/:role
@acess  Public 
*/
exports.login = asyncHandler(async (req, res, next) => {
  const role = req.params.role;

  // 1-) Check if email exists and password is correct and same role
  const user = await User.findOne({ email: req.body.email, role });
  if (!user || !(await bcrypt.compare(req.body.password, user.password)))
    return next(new ApiError("Incorrect email or password", 401));

  // 3-) Generate jwt token
  const token = generateTwtToken(user._id);

  res.status(200).json({ data: user, token });
});

/*
@desc   Get User By ID
@route  GET /user/:id
@acess  Public
*/
exports.getUser = asyncHandler(async (req, res, next) => {
  const id = req.params.id;

  const user = await User.findById(id);

  if(!user) return next(new ApiError("User not found", 404));



  res.status(200).json({ data: user });
});


