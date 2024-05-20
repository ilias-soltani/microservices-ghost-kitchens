const path = require("path");
const nodemailer = require("nodemailer");
const hbs = require("nodemailer-express-handlebars");

const sendEmail = async (options, context) => {
    let transporter = nodemailer.createTransport({
        host: process.env.EMAIL_HOST,
        port: process.env.EMAIL_PORT,
        secure: true,
        auth: {
            user: process.env.EMAIL_USER,
            pass: process.env.EMAIL_PASSWORD,
        },
    });

    const hbsOpt = {
        viewEngine: {
            extName: ".handlebars",
            partialsDir: path.resolve(__dirname, "./views"),
            defaultLayout: false, // <-----   added this
        },
        viewPath: path.resolve(__dirname, "./views"),
        extName: ".handlebars",
    };

    transporter.use("compile", hbs(hbsOpt));

    const opt = {
        from: "Buy-it <buy.it.production@gmail.com>",
        to: options.to,
        subject: options.subject,
        template: options.template,
        context,
        /*  attachments: [
          {
            filename: "lock.png",
            path: path.resolve(__dirname, "./src/lock.png"),
            cid: "lock", //my mistake was putting "cid:logo@cid" here!
          },
        ],*/
    };

    await transporter.sendMail(opt);
};

module.exports = sendEmail;
