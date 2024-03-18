import express from "express";
import {
    getData,
    createData,
    getWeather
} from "../controllers/Irrigation.js";


const router = express.Router();

router.get('/data',getData);
router.post('/data',createData);
router.get('/weather',getWeather);


export default router;
