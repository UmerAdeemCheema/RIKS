const express = require("express");
const morgan = require("morgan");
const cors = require("cors");
const connectDB = require("./config/db");
const passport = require("passport");
const bodyParser = require("body-parser");
const routes = require('./routes/rroutes')

connectDB()

var http = require("http");
const app = express();

if(process.env.NODE_ENV === 'development'){
    app.use(morgan('dev'))
}

const port = process.env.PORT || 5000;
// var server = http.createServer(app);
// var io = require("socket.io")(server);


//middleware
app.use("/uploads", express.static("uploads"))
app.use(express.json());
app.use(bodyParser.urlencoded({extended:false}));
app.use(bodyParser.json());
app.use(cors());
app.use(routes);
app.use(passport.initialize());
require('./config/passport')(passport);


// var clients = {};

// io.on("connection", (socket) => {
//   console.log("connetetd");
//   console.log(socket.id, "has joined");
//   socket.on("signin", (id) => {
//     console.log(id);
//     clients[id] = socket;
//     console.log(clients);
//   });
//   socket.on("message", (msg) => {
//     console.log(msg);
//     let targetId = msg.targetId;
//     if (clients[targetId]) clients[targetId].emit("message", msg);
//   });
// });

// server.listen(port, "0.0.0.0", ()=>{
//     console.log("Server Started");
// })

app.listen(port, console.log(`Server running in ${process.env.NODE_ENV} mode on port ${port}`))


const SocketServer = require('websocket').server

const server = http.createServer((req, res) => {})

server.listen(3000, ()=>{
    console.log("Listening on port 3000...")
})

wsServer = new SocketServer({httpServer:server})

const connections = []

wsServer.on('request', (req) => {
    const connection = req.accept()
    connections.push(connection)

    connection.on('message', (mes) => {
        connections.forEach(element => {
            if (element != connection)
                element.sendUTF(mes.utf8Data)
        })
    })

    connection.on('close', (resCode, des) => {
        connections.splice(connections.indexOf(connection), 1)
    })

})