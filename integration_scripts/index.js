
'use strict';

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);
// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });


exports.notifyGardenOwner = functions.database.ref('/gardens/{gardenUId}/gMembers/{memberuserid}').onWrite(event => {

const gardenUId = event.params.gardenUId;
const memberuserid = event.params.memberuserid;
const currentdata = event.data.val();

console.log('Data in the event ',currentdata);

if(null == currentdata){
 return console.log('User ', memberuserid, 'Cancelled Membership for ', gardenUId);
} else if(false == currentdata){

console.log('User ', memberuserid, 'Requested Membership for ', gardenUId);

var db = admin.database();
var gardenOwner;
var devicetoken;
var refnode = '/gardens/'+gardenUId+'/gOwner';
console.log(refnode);
var ref = db.ref(`/gardens/${gardenUId}/gOwner`);

  //const gardenOwner = admin.database().ref('/gardens/{gardenUId}/gOwner').once('value')
  // Get the Owner of the Garden
  ref.on("value", function(snapshot) {
    gardenOwner = snapshot.val();
    console.log('Garden Owner ',gardenOwner);

    var tokenRef = '/'+gardenOwner+'/token';
    console.log('Token reference ',tokenRef);
    // Get the device notification token.
    var gardenOwnerDeviceToken = admin.database().ref(`/${gardenOwner}/token`);

    gardenOwnerDeviceToken.on("value", function(snapshot) {
      devicetoken = snapshot.val();
      console.log(devicetoken);

      var getRequestor = admin.auth().getUser(memberuserid);

      return Promise.all([snapshot,getRequestor]).then(results => {
        const tokensSnapshot = results[0];
        const requestor = results[1];

        console.log('Token ', tokensSnapshot);
        console.log('Requestor ', requestor);

        var gardenNameReference = admin.database().ref(`/gardens/${gardenUId}/gName`);

        gardenNameReference.on("value",function(snapshot) {
          var gardenName = snapshot.val();
          console.log(gardenName);
          const payload = {
            notification: {
              title: `New Membership Request for ${gardenName}!`,
              body: `${requestor.email}  is requesting to Collaborate in your Garden.`,
            }
          };

          admin.messaging().sendToDevice(devicetoken, payload)
          .then(function(response) {

        console.log("Successfully sent message:", response);
          })
          .catch(function(error) {
        console.log("Error sending message:", error);
        });

        }, function (errorObject) {
          console.log("The read failed: " + errorObject.code);
        });



      });

    }, function (errorObject) {
      console.log("The read failed: " + errorObject.code);
    });

  }, function (errorObject) {
    console.log("The read failed: " + errorObject.code);
  });
}else if(true == currentdata){

  console.log('Your membership has been approved for ', gardenUId);

  var requestorDeviceToken = admin.database().ref(`/${memberuserid}/token`);
  var reqToken;
  requestorDeviceToken.on("value", function(snapshot) {

    reqToken=snapshot.val();
    console.log(reqToken);

    var gardenNameReference = admin.database().ref(`/gardens/${gardenUId}/gName`);

    gardenNameReference.on("value",function(snapshot) {

      var gardenName = snapshot.val();
      console.log(gardenName);
      const payload = {
        notification: {
          title: `Membership Approval for ${gardenName}`,
          body: `Congratulations!! Your membership has been approved by the Garden Owner`,
        }
      };

        admin.messaging().sendToDevice(reqToken, payload).then(function(response) {
            console.log("Successfully sent message:", response);
          })
          .catch(function(error) {
            console.log("Error sending message:", error);
          });

    }, function (errorObject) {
      console.log("The read failed: " + errorObject.code);
    });

  }, function (errorObject) {
    console.log("The read failed: " + errorObject.code);
  });
}
});

exports.vitalStatNotifier = functions.database.ref('/{userId}/Plants/{plantid}/thresholdValues/{sensorname}').onWrite(event => {

  const plantownerId = event.params.userId;
  const plantId = event.params.plantid;
  const sensorid = event.params.sensorname;

  const conductivity = "Soil Nutrition";
  const lightexposure = "Light Exposure";
  const waterLevel = "Needs Water";
  const temprature = "Temperature";

  var sendNotification = false;
  var sensor;

  const currentdata = event.data.val();

  if(null == currentdata){
    return console.log("No Data Found");
  }

  console.log("Owner ",plantownerId,"Plant Id: ",plantId);
  var ownerToken = admin.database().ref(`/${plantownerId}/token`);

  var token;
  ownerToken.on("value",function(snapshot) {

    var token = snapshot.val();
    console.log(token);

    var plantNameref = admin.database().ref(`/${plantownerId}/Plants/${plantId}/plantName`);

    plantNameref.on("value",function(snapshot) {

      var plantName = snapshot.val();

      console.log("plantName",plantName);

      if('ph' == sensorid){

        if (currentdata > 2){
          sendNotification = true;
          sensor = conductivity;
        }

      }else if('sunlight' == sensorid){

        if (currentdata > 50000 || currentdata < 25000){
          sendNotification = true;
          sensor = lightexposure;
        }
      }else if('moisture' == sensorid){

        if (currentdata > 50){
          sendNotification = true;
          sensor = waterLevel;
        }

      }else if('temperature' == sensorid){
        if(currentdata < 30 || currentdata > 100){
          sendNotification = true;
          sensor = temprature;
        }

      }

      if(true == sendNotification){
      const payload = {
        notification: {
          title: `${plantName} needs attention!!`,
          body: `Your plant ${plantName} needs attention!! ${sensor} has breached threshold `,
        }
      };

      admin.messaging().sendToDevice(token, payload).then(function(response) {
            console.log("Successfully sent message:", response);
          })
          .catch(function(error) {
            console.log("Error sending message:", error);
      });

    }

    }, function (errorObject) {
      console.log("The read failed: " + errorObject.code);
    });

  }, function (errorObject) {
    console.log("The read failed: " + errorObject.code);
  });

});
