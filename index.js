//Import Library
import db from "./config/Database.js";
import express from "express";
import bodyParser from "body-parser";
import IrrigationRoute from "./routes/IrrigationRoute.js";
import cors from "cors";


const app = express();
const PORT = process.env.PORT || 3030;

app.use(cors({
  credentials: true,
}));


app.use(bodyParser.urlencoded({ extended: false }))
app.use(bodyParser.json())
app.use(IrrigationRoute)


// Memastikan koneksi dengan database
db.sync({ force: false })
  .then(() => {
    console.log("Database berhasil terhubung");
    // Memastikan server berhasil berjalan di port yang dipilih
    app.listen(PORT, (error) => {
      if (!error)
        console.log("Server berjalan di port " + PORT)
      else
        console.log("Terjadi kesalahan , server eror", error);
    });
  })
  .catch((error) => {
    console.error("Database Gagal Terhubung", error);
  });
