const mongoose = require("mongoose");
const bcrypt = require("bcryptjs");

const userSchema = new mongoose.Schema(
  {
    name: {
      type: String,
      trim: true,
      lowercase: true,
      required: true,
    },
    email: {
      type: String,
      required: true,
      trim: true,
      lowercase: true,
      unique: true,
    },
    password: {
      type: String,
      minLength: [8, "Password should be at least 8 characters"],
    },
    role: {
      type: String,
      enum: ["admin", "client", "chef", "agency"],
      default: "client",
    },
    phoneNumber: {
      type: String,
      trim: true,
      required: true,
      unique: true,
    },
    verified: {
      type: Boolean,
      default: true,
    },
    status: {
        type: String,
        enum: ["valid", "pending", "late"],
        default: "valid",
    },
    firstTime: {
        type: Boolean,
        default: true,
    },
    passwordChangedAt: Date,
    passwordRestCode: String,
    passwordRestCodeExpires: Date,
    passwordRestCodeVerified: Boolean,
    verifyEmailCode: String,
    verifyEmailCodeExpires: Date,
    paymentAt: Date
  },
  { timestamps: true }
);

userSchema.pre("save", async function (next) {
  if (this.isModified("password"))
    this.password = await bcrypt.hash(this.password, 12);

  next();
});

const User = mongoose.model("User", userSchema);

module.exports = User;
