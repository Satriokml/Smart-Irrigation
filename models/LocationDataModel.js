import { Sequelize } from "sequelize";
import db from "../config/Database.js";

const {DataTypes} = Sequelize;

const LocationDataModel = db.define('location',{
    id:{
        primaryKey: true,
        type: DataTypes.INTEGER,
        allowNull: false,
    },
    latitude:{
        type: DataTypes.DECIMAL(10,7),
        allowNull: true,
    },
    longitude:{
        type: DataTypes.DECIMAL(10,7),
        allowNull: true,
    },
},
{
    freezeTableName: true
});

export default LocationDataModel;