import { Sequelize } from "sequelize";
import db from "../config/Database.js";


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
    intercept:{
        type: DataTypes.FLOAT,
        allowNull: false,
    },
    slope:{
        type: DataTypes.FLOAT,
        allowNull: false,
    },
    tdry:{
        type: DataTypes.FLOAT,
        allowNull:true
    },
    tnws:{
        type: DataTypes.FLOAT,
        allowNull:true
    }
},
{
    freezeTableName: true
});



export default PlantsDataModel;