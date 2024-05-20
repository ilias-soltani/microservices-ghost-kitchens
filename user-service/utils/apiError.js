// This class is used to represent errors is simple way

class ApiError extends Error {
  constructor(message, status) {
    super(message);
    this.status = status;
  }
}

module.exports = ApiError;
