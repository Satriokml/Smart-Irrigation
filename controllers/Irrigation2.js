import IrrigationDataModel2 from "../models/IrrigationDataModel2.js";
import axios from "axios";


const WEATHER_API_KEY = process.env.WEATHER_API_KEY;
const WEATHER_API_URL = process.env.WEATHER_API_URL;


export const getData2 = async(req, res) =>{
    try {
        let response;
        response = await IrrigationDataModel2.findAll();
        res.status(200).json(response);
    } catch (error) {
        res.status(500).json({msg: error.message});
    }
}

export const getLastest2 = async(req, res) =>{
    try {
        let response;
        response = await IrrigationDataModel2.findOne({
            order:[['id', 'DESC']]
        });
        res.status(200).json(response);
    } catch (error) {
        res.status(500).json({msg: error.message});
    }
}


export const createData2 = async(req, res) =>{
    const {canopy_temperature, air_temperature, soil_moisture, relative_humidity, cwsi, weather_prediction, decision} = req.body;
    try {
        await IrrigationDataModel2.create({
            canopy_temperature:canopy_temperature,
            air_temperature:air_temperature,
            soil_moisture:soil_moisture,
            relative_humidity:relative_humidity,
            cwsi : cwsi,
            weather_prediction:weather_prediction,
            decision:decision

        });
        res.status(201).json({msg: "Post Created Successfuly"});
    } catch (error) {
        res.status(500).json({msg: error.message});
    }
}
