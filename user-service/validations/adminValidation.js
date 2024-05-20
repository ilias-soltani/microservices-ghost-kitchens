const { check } = require("express-validator");

const validationMiddelware = require("../middlewares/validationMiddelware.js");

exports.updateChefStatusValidation = [
    check("id")
        .isMongoId()
        .withMessage("Please entre a valid chef id"),
    check("verified")
        .notEmpty()
        .withMessage("Verified is required")
        .isBoolean()
        .withMessage("Please entre a valid verified value"),
    validationMiddelware,
];

exports.updateChefPaymentStatusValidation = [
    check("id")
        .isMongoId()
        .withMessage("Please entre a valid chef id"),
    check("status")
        .notEmpty()
        .withMessage("Status is required")
        .custom((status) => {
            const values = ["valid", "pending", "late"];
            if (!values.includes(status)) throw new Error("Invalid status!");
            return true;
        }),
    validationMiddelware,
];