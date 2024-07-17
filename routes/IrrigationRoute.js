import express from "express";
import {
    getData,
    createData,
    getWeather,
    getLastest,
    getLastestCWSI
} from "../controllers/Irrigation.js";

import {
    getData2,
    createData2,
    getLastest2
} from "../controllers/Irrigation2.js";

import {
    getBaseline,
    updatePlantActive,
    getLocation,
    updateLocation
} from "../controllers/Data.js";


const router = express.Router();

router.get('/data',getData);
router.get('/lastest',getLastest);
router.get('/lastest_cwsi',getLastestCWSI);
router.post('/data',createData);


router.get('/data2',getData2);
router.get('/lastest2',getLastest2);
router.post('/data2',createData2);

router.get('/baseline',getBaseline);
router.patch('/plant', updatePlantActive);

router.get('/weather',getWeather);

router.get('/location',getLocation);
router.patch('/location',updateLocation);


export default router;
