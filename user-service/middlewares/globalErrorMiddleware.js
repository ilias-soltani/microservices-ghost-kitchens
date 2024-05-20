const globalErrorMiddleware = (error, req, res, next) => {
  error.status = error.status || 500;

  process.env.NODE_ENV == "development"
    ? res
        .status(error.status)
        .json({ error: error.message, stack: error.stack })
    : res.status(error.status).json({ error: error.message });
};

module.exports = globalErrorMiddleware;
