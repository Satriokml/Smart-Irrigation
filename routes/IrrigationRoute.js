import express from "express";
import {
    getData,
    createData,
    getWeather,
    getLastest
} from "../controllers/Irrigation.js";

import {
    getData2,
    createData2,
    getLastest2
} from "../controllers/Irrigation2.js";


const router = express.Router();

router.get('/data',getData);
router.get('/lastest',getLastest);
router.post('/data',createData);
router.get('/data2',getData2);
router.get('/lastest2',getLastest2);
router.post('/data2',createData2);
router.get('/weather',getWeather);


export default router;
