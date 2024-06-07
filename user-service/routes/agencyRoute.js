const express = require("express");

const {
    createAgency
} = require("../services/agencyService");
const {
   createAgencyValidation
} = require("../validations/authValidation");

const {
    auth,
    access
} = require("../middlewares/authMiddleware")


const router = express.Router();

router.post("/", auth, access("admin"), createAgencyValidation , createAgency);

module.exports = router;