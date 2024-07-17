import PlantActiveDataModel from "../models/PlantActiveDataModel.js";
import PlantsDataModel from "../models/PlantsDataModel.js";
import LocationDataModel from "../models/LocationDataModel.js";

export const getBaseline = async(req, res) =>{
    try {
        let response;
        response = await PlantActiveDataModel.findAll({
            include: [
              {
                model: PlantsDataModel,
                attributes: ['name', 'intercept', 'slope', 'tdry', 'tnws']
              }
            ],
            attributes: ['id']
          })
        res.status(200).json(response);
    } catch (error) {
        res.status(500).json({msg: error.message});
    }
}

export const updatePlantActive = async (req, res) => {
    const { plant_data_id } = req.body;

    try {
        await PlantActiveDataModel.update(
        { plant_data_id: plant_data_id }, // Set the fields you want to update
        { where: { id: 1 } } // Condition to identify the row(s) to be updated
      );
      res.status(200).json({ msg: 'Plant active updated successfully' });

    } catch (error) {
      console.error('Error updating plant active:', error);
      res.status(500).json({ msg: error.message });
    }
  };

  export const getLocation = async(req, res) =>{
    try {
        let response;
        response = await LocationDataModel.findOne({
          attributes: ['latitude', 'longitude'],
          })
        res.status(200).json(response);
    } catch (error) {
        res.status(500).json({msg: error.message});
    }
}

export const updateLocation = async (req, res) => {
  const { latitude, longitude } = req.body;

  try {
      await LocationDataModel.update(
      { latitude: latitude,
        longitude: longitude
       }, // Set the fields you want to update
      { where: { id: 1 } } // Condition to identify the row(s) to be updated
    );
    res.status(200).json({ msg: 'location updated successfully' });

  } catch (error) {
    console.error('Error updating location:', error);
    res.status(500).json({ msg: error.message });
  }
};