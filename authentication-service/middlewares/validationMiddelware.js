const { validationResult } = require("express-validator");

const validationMiddelware = (req, res, next) => {
  const result = validationResult(req);
  if (result.isEmpty()) {
    return next();
  }

  res.status(400).send({ errors: result.array() });
};

module.exports = validationMiddelware;
