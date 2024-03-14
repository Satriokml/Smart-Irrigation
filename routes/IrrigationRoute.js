import express from "express";
import {
    getData,
    createData
} from "../controllers/Irrigation.js";


const router = express.Router();

router.get('/data',getData);
router.post('/data',createData);


export default router;