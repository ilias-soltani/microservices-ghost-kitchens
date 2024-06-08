const express = require("express");

const {
    createAgency,
    updateAgencyStatus
} = require("../services/agencyService");
const {
   createAgencyValidation
} = require("../validations/authValidation");

const {
    updateChefStatusValidation
} = require("../validations/adminValidation");

const {
    auth,
    access
} = require("../middlewares/authMiddleware")


const router = express.Router();

router.post("/", auth, access("admin"), createAgencyValidation , createAgency);

router.patch("/update-agency-status/:id", auth, access("admin"), updateChefStatusValidation , updateAgencyStatus);

module.exports = router;