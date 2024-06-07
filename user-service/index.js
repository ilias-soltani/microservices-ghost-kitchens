const dotenv = require("dotenv");
const express = require("express");
const cors = require("cors");
const compression = require("compression");
const morgan = require("morgan");

const eurekaHelper = require("./eureka-client");
const ApiError = require("./utils/apiError");
const globalErrorMiddleware = require("./middlewares/globalErrorMiddleware");
const dbConnection = require("./config/database");
const authRouter = require("./routes/authRoute");
const jwtRouter = require("./routes/jwtRoute");
const adminRouter = require("./routes/adminRoute");
const chefRouter = require("./routes/chefRoute");
const restaurantRouter = require("./routes/restaurantRoute");
const agencyRouter = require("./routes/agencyRoute");

// Configurations
dotenv.config({ path: ".env" });

const app = express();
app.use(express.json());

// Enable other domains to access this api
app.use(cors());
app.options("*", cors());

// Commprese all requests
app.use(compression());

// Request listener
app.use(morgan("dev"));

// Connect to database
dbConnection();

// Set up routers
const mainPath = "/api/v1";

app.use(`${mainPath}`, authRouter);
app.use(`${mainPath}/jwt`, jwtRouter);
app.use(`${mainPath}/admin`, adminRouter);
app.use(`${mainPath}/chef`, chefRouter);
app.use(`${mainPath}/restaurants`, restaurantRouter);
app.use(`${mainPath}/agency`, agencyRouter);

// Error handling if the route doesn't exist
app.all("*", (req, res, next) => {
  next(new ApiError(`Can't find this route ${req.originalUrl}`, 404));
});

// Error handling middleware (the app will use this if there is error)
app.use(globalErrorMiddleware);

const port = process.env.PORT || 8000;
const server = app.listen(port, () => {
  console.log(`App is running on port: ${port}`);
});

eurekaHelper.registerWithEureka("user-service", port);

// handel promise without catch
process.on("unhandledRejection", (e) => {
  console.error(` unhandledRejection ERROR: ${e.name} | ${e.message}`);
});
