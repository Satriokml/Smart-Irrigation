//Import Library yang diperlukan
import { Sequelize } from "sequelize";

//Import fungsi db dari database.js yang telah dijelaskan
import db from "../config/Database.js";
import PlantsDataModel from "./PlantsDataModel.js";

const {DataTypes} = Sequelize;

const IrrigationDataModel = db.define('irrigation_data',{ //Masukkan nama tabel
    //Input data-data apa saja yang diperlukan dalam sebuah tabel
    canopy_temperature:{
        type: DataTypes.FLOAT,
        allowNull: false, //Data tidak boleh null / kosong
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
    },
    plant_data_id:{
        type: DataTypes.INTEGER,
        allowNull: false,
    }

},
{
    freezeTableName: true
});

IrrigationDataModel.belongsTo(PlantsDataModel, { foreignKey: 'plant_data_id' });
PlantsDataModel.hasOne(IrrigationDataModel, { foreignKey: 'plant_data_id' });

//export model ini agar bisa digunakan oleh fungsi lain
export default IrrigationDataModel;