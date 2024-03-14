import {Sequelize} from "sequelize";

const db = new Sequelize('defaultdb', 'avnadmin', 'AVNS_FT_-_8_SXel5N_6gkV4', {
    host: "mysql-irrigaitonsys-satriokamil3-c798.a.aivencloud.com",
    dialect: "mysql",
    port: 10010,
    timezone: '+07:00'
});

export default db;