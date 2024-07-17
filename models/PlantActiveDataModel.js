import { Sequelize } from "sequelize";
import db from "../config/Database.js";
import PlantsDataModel from "./PlantsDataModel.js";

const {DataTypes} = Sequelize;

const PlantActiveDataModel = db.define('plant_active',{
    id:{
        primaryKey: true,
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

PlantActiveDataModel.belongsTo(PlantsDataModel, { foreignKey: 'plant_data_id' });
PlantsDataModel.hasMany(PlantActiveDataModel, { foreignKey: 'plant_data_id' });

export default PlantActiveDataModel;