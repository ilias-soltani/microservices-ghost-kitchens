const express = require("express");

const {
    updateChefStatus,
    getUsers,
    updateChefPaymnet
} = require("../services/adminService")

const {
    updateChefStatusValidation,
    updateChefPaymentStatusValidation
} = require("../validations/adminValidation")

const {
    auth,
    access
} = require("../middlewares/authMiddleware")

const router = express.Router();


router.post("/update-chef-status/:id", auth, access("admin"), updateChefStatusValidation, updateChefStatus);

router.post("/update-chef-payment-status/:id", auth, access("admin"),
    updateChefPaymentStatusValidation, updateChefPaymnet);

router.get("/users", auth, access("admin"), getUsers);

module.exports =router;