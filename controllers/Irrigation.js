import IrrigationDataModel from "../models/IrrigationDataModel.js";


export const getData = async(req, res) =>{
    try {
        let response;
        response = await IrrigationDataModel.findAll();
        res.status(200).json(response);
    } catch (error) {
        res.status(500).json({msg: error.message});
    }
}

export const createData = async(req, res) =>{
    const {canopy_temperature, air_temperature, soil_moisture, relative_humidity, cwsi, weather_prediction, decision} = req.body;
    try {
        await IrrigationDataModel.create({
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
