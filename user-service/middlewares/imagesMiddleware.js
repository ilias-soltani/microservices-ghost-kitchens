const multer = require("multer");

const ApiError = require("../utils/apiError");

const init = () => {
  // --) Memory Storage
  const storage = multer.memoryStorage();

  // --) File filter
  const fileFilter = (req, file, cb) => {
    if (file.mimetype.startsWith("image")) return cb(null, true);
    cb(new ApiError("Only images are allowed", 400), false);
  };

  // --) Init upload
  const upload = multer({ storage, fileFilter });

  return upload;
};

exports.uploadSingleImage = (field) => init().single(field);

exports.uploadMixedImages = (fields) => init().fields(fields);
