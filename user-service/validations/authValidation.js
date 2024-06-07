const { check } = require("express-validator");

const validationMiddelware = require("../middlewares/validationMiddelware.js");

exports.signupValidation = [
  check("name")
    .notEmpty()
    .withMessage("Name is required")
    .isLength({ min: 2 })
    .withMessage("Name is too short")
    .isLength({ max: 32 })
    .withMessage("User name is too long"),
  check("email")
    .notEmpty()
    .withMessage("User email is required")
    .isEmail()
    .withMessage("Please entre valid email"),
  check("password")
    .notEmpty()
    .withMessage("User password is required")
    .isLength({ min: 8 })
    .withMessage("User password should be at least 8 characters"),
  check("phoneNumber")
    .notEmpty()
    .withMessage("User phone number is required")
    .isMobilePhone("ar-DZ")
    .withMessage("Please entre valid phone number"),
  check("role")
    .optional()
    .custom((role) => {
      const roles = ["admin", "client", "agency"];
      if (!roles.includes(role)) throw new Error("Invalid user role!");
      return true;
    }),
  validationMiddelware,
];

exports.createAgencyValidation = [
    check("name")
        .notEmpty()
        .withMessage("Name is required")
        .isLength({ min: 2 })
        .withMessage("Name is too short")
        .isLength({ max: 32 })
        .withMessage("User name is too long"),
    check("email")
        .notEmpty()
        .withMessage("User email is required")
        .isEmail()
        .withMessage("Please entre valid email"),
    check("password")
        .notEmpty()
        .withMessage("User password is required")
        .isLength({ min: 8 })
        .withMessage("User password should be at least 8 characters"),
    check("phoneNumber")
        .notEmpty()
        .withMessage("User phone number is required")
        .isMobilePhone("ar-DZ")
        .withMessage("Please entre valid phone number"),
    check("wilaya")
        .notEmpty()
        .withMessage("Wilaya is required"),
    validationMiddelware,
];

exports.signupChefValidation = [
  check("name")
    .notEmpty()
    .withMessage("Name is required")
    .isLength({ min: 2 })
    .withMessage("Name is too short")
    .isLength({ max: 32 })
    .withMessage("User name is too long"),
  check("email")
    .notEmpty()
    .withMessage("User email is required")
    .isEmail()
    .withMessage("Please entre valid email"),
  check("password")
    .notEmpty()
    .withMessage("User password is required")
    .isLength({ min: 8 })
    .withMessage("User password should be at least 8 characters"),
  check("phoneNumber")
    .notEmpty()
    .withMessage("User phone number is required")
    .isMobilePhone("ar-DZ")
    .withMessage("Please entre valid phone number"),
  check("restaurantName")
    .notEmpty()
    .withMessage("Restaurant name is required")
    .isLength({ min: 2 })
    .withMessage("Restaurant Name is too short")
    .isLength({ max: 32 })
    .withMessage("Restaurant name is too long"),
  check("ccp").notEmpty().withMessage("Ccp is required"),
  check("address.wilaya").notEmpty().withMessage("Wilaya is required"),
  check("address.baladiya").notEmpty().withMessage("Baladiya is required"),
  check("address.street").notEmpty().withMessage("Street is required"),
  validationMiddelware,
];

exports.loginValidation = [
  check("email")
    .notEmpty()
    .withMessage("User email is required")
    .isEmail()
    .withMessage("Please entre valid email"),
  check("password")
    .notEmpty()
    .withMessage("User password is required")
    .isLength({ min: 8 })
    .withMessage("User password should be at least 8 characters"),
  check("role")
    .notEmpty()
    .withMessage("User role is required")
    .custom((role) => {
      const roles = ["admin", "client", "chef", "agency"];
      if (!roles.includes(role)) throw new Error("Invalid user role!");
      return true;
    }),
  validationMiddelware,
];


exports.getUserValidation = [
    check("id").isMongoId().withMessage("Please entre a valid user id"),
    validationMiddelware,
];
