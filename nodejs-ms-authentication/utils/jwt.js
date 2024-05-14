const jwt = require("jsonwebtoken");

const generateTwtToken = (id) =>
  jwt.sign({ userId: id }, process.env.JWT_SECRET_KEY, {
    expiresIn: process.env.TOKEN_EXPIRES_IN,
  });

module.exports = generateTwtToken;
