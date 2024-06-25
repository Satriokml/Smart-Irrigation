import IrrigationDataModel from "../models/IrrigationDataModel.js";
import axios from "axios";


const WEATHER_API_KEY = process.env.WEATHER_API_KEY;
const WEATHER_API_URL = process.env.WEATHER_API_URL;


export const getData = async(req, res) =>{
    try {
        let response;
        response = await IrrigationDataModel.findAll();
        res.status(200).json(response);
    } catch (error) {
        res.status(500).json({msg: error.message});
    }
}

export const getLastest = async(req, res) =>{
    try {
        let response;
        response = await IrrigationDataModel.findOne({
            order:[['id', 'DESC']]
        });
        res.status(200).json(response);
    } catch (error) {
        res.status(500).json({msg: error.message});
    }
}

export const getLastestCWSI = async(req, res) =>{
    try {
        let response;
        response = await IrrigationDataModel.findAll({
            attributes: ['cwsi', 'createdAt'],
            order:[['id', 'DESC']],
            limit:10
        });
        res.status(200).json(response);
    } catch (error) {
        res.status(500).json({msg: error.message});
    }
}

export const getWeather = async(req, res) =>{
    try {
        const latitude = process.env.latitude;
        const longitude = process.env.longitude;

        const response = await axios.get(WEATHER_API_URL, {
          params: {
            key: WEATHER_API_KEY,
            q: `${latitude},${longitude}`,
            aqi: 'no'
          }
        });

        const weatherData = response.data;
        //res.json(weatherData.current.condition);
        res.json(weatherData.current);
      } catch (error) {

        console.error('Error fetching weather data:', error);
        res.status(500).json({ error: 'Error fetching weather data' });
      }
}

export const createData = async(req, res) =>{
    const {canopy_temperature, air_temperature, soil_moisture, relative_humidity, cwsi, decision} = req.body;
    try {
        const latitude = process.env.latitude;
        const longitude = process.env.longitude;

        const response = await axios.get(WEATHER_API_URL, {
          params: {
            key: WEATHER_API_KEY,
            q: `${latitude},${longitude}`,
            aqi: 'no'
          }
        });

        const weatherData = response.data;
        await IrrigationDataModel.create({
            canopy_temperature:canopy_temperature,
            air_temperature:air_temperature,
            soil_moisture:soil_moisture,
            relative_humidity:relative_humidity,
            cwsi : cwsi,
            weather_prediction:weatherData.current.condition.text,
            decision:decision

        });
        res.status(201).json({msg: "Post Created Successfuly"});
    } catch (error) {
        res.status(500).json({msg: error.message});
    }
}
