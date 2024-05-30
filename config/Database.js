import {Sequelize} from "sequelize";
import dotenv from "dotenv";
dotenv.config();

const host = process.env.DB_HOST
const name = process.env.DB_NAME
const user = process.env.DB_USER
const pass = process.env.DB_PASS

const db = new Sequelize(name, user, pass, {
    host: host,
    dialect: "mysql",
    port: process.env.DB_PORT,
    dialectOptions: {
        useUTC: false, //for reading from database
        dateStrings: true,
        typeCast: function (field, next) { // for reading from database
            if (field.type === 'DATETIME') {
                return field.string()
            }
            return next()
        },
    },
    timezone: '+07:00'
});


export default db;
