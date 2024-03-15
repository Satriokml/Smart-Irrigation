import db from "./config/Database.js";
import express from "express";
import dotenv from "dotenv";
import bodyParser from "body-parser";
import IrrigationRoute from "./routes/IrrigationRoute.js";
import cors from "cors";
import { Sequelize } from "sequelize";
import IrrigationDataModel from "./models/IrrigationDataModel.js";

const app = express();
const PORT = process.env.PORT || 3030;

app.use(cors({
  credentials: true,
}));


app.use(bodyParser.urlencoded({ extended: false }))
app.use(bodyParser.json())
app.use(IrrigationRoute)


// Sync the model with the database
db.sync({ force: false }) // Set force to true to drop tables before recreating
  .then(() => {
    console.log("Database synced successfully");
    // Start your server after syncing the database
    app.listen(PORT, (error) => {
      if (!error)
        console.log("Server is Successfully Running, and App is listening on port " + PORT)
      else
        console.log("Error occurred, server can't start", error);
    });
  })
  .catch((error) => {
    console.error("Error syncing database", error);
  });
