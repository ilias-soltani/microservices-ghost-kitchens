const sharp = require("sharp");
const {
  getStorage,
  ref,
  getDownloadURL,
  uploadBytesResumable,
  deleteObject,
} = require("firebase/storage");

const app = require("./config.js");

const storage = getStorage(app);

exports.uploadImage = async (bufferArray, image, fileName = "restaurants") => {
  const buffer = await sharp(bufferArray).webp({ quality: 90 }).toBuffer();

  const storageRef = ref(storage, `uploads/${fileName}/${image}`);
  const snapshot = await uploadBytesResumable(storageRef, buffer, {
    contentType: "image/webp",
  });

  const url = await getDownloadURL(snapshot.ref);
  return url;
};
