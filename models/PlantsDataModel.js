import { Sequelize } from "sequelize";
import db from "../config/Database.js";
import PlantActiveDataModel from "./PlantActiveDataModel.js";

const {DataTypes} = Sequelize;

const PlantsDataModel = db.define('plants_data',{
    id:{
        primaryKey:true,
        type: DataTypes.INTEGER,
        allowNull: false,
    },
    name:{
        type: DataTypes.STRING,
        allowNull: false,
    },
    upper_baseline:{
        type: DataTypes.FLOAT,
        allowNull: false,
    },
    lower_baseline:{
        type: DataTypes.FLOAT,
        allowNull: false,
    }
},
{
    freezeTableName: true
});



export default PlantsDataModel;