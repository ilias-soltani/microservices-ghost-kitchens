const { check } = require("express-validator");

const validationMiddelware = require("../middlewares/validationMiddelware.js");

exports.accessValidation = [
    check("roles")
        .notEmpty()
        .withMessage("Please entre the valid roles!")
        .isArray()
        .withMessage("Please entre a valid roles array!"),
    validationMiddelware,
];
