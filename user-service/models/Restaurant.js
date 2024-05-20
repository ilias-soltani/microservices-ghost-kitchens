const mongoose = require("mongoose");

const restaurantSchema = new mongoose.Schema(
  {
    name: {
      type: String,
      trim: true,
      lowercase: true,
      required: true,
      unique: true,
    },
    logo: {
      type: String,
      trim: true,
      lowercase: true,
      required: true,
    },
    ccp: {
      type: String,
      trim: true,
      lowercase: true,
      required: true,
    },
    address: {
      wilaya: {
        type: String,
        trim: true,
        lowercase: true,
        required: true,
      },
      baladiya: {
        type: String,
        trim: true,
        lowercase: true,
        required: true,
      },
      street: {
        type: String,
        trim: true,
        lowercase: true,
        required: true,
      },
    },
    verified: {
      type: Boolean,
      default: false,
    },
    user: {
      type: mongoose.Schema.ObjectId,
      ref: "User",
      required: true,
      unique: true,
    },
  },
  { timestamps: true }
);

const Restaurant = mongoose.model("Restaurant", restaurantSchema);

module.exports = Restaurant;
