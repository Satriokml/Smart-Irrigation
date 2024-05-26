import { Sequelize } from "sequelize";
import db from "../config/Database.js";

const {DataTypes} = Sequelize;

const IrrigationDataModel2 = db.define('irrigation_data_2',{
    canopy_temperature:{
        type: DataTypes.FLOAT,
        allowNull: false,
    },
    air_temperature:{
        type: DataTypes.FLOAT,
        allowNull: false,
    },
    soil_moisture:{
        type: DataTypes.FLOAT,
        allowNull: false,
    },
    relative_humidity:{
        type: DataTypes.FLOAT,
        allowNull: false,
    },
    cwsi:{
        type: DataTypes.FLOAT,
        allowNull: false,
    },
    weather_prediction:{
        type: DataTypes.STRING,
        allowNull: false,
    },
    decision:{
        type: DataTypes.INTEGER,
        allowNull: false,
    }

},
{
    freezeTableName: true
});

export default IrrigationDataModel2;