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
    wilaya: {
        type: String,
        trim: true,
        lowercase: true,
    },
    nationalCard: {
        photo: {
            type: String,
            trim: true,
            lowercase: true
        },
        cardNumber: {
            type: String,
            trim: true,
        },
        issuedBy: {
            type: String,
            trim: true,
        },
        releaseDate: {
            type: String,
            trim: true,
        },
        expiryDate: {
            type: String,
            trim: true,
        },
        nationalNumber: {
            type: String,
            trim: true,
        },
        surname: {
            type: String,
            trim: true,
        },
        name: {
            type: String,
            trim: true,
        },
        birthDate: {
            type: String,
            trim: true,
        },
        sex: {
            type: String,
            trim: true,
        },
        rh: {
            type: String,
            trim: true,
        },
        birthPlace: {
            type: String,
            trim: true,
        }
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
