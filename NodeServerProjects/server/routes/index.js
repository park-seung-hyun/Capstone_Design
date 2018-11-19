const express = require('express');
const router = express.Router();
var passport = require('passport');
const passportConfig = require('../../config/passport');
var User = require('../models/user');
var Content = require('../models/content');
var App = require('../models/app');
var jwt = require('jwt-simple'); // jwt token 사용
var mkdirp = require('mkdirp'); // directory 만드는것
var nodemailer = require('nodemailer');

router.post('/jwtCheck', function(req, res){

  console.log("jwtCheck jwt토큰 "+ req.body.token);
  var decoded = jwt.decode(req.body.token,req.app.get("jwtTokenSecret"));
  console.log("jwtCheck jwt토큰 디코딩 "+ decoded.userCheck);
  var email = decoded.userCheck;

  User.findOne({ email : email }, function(err, user) {
    console.log(user);
    if(err){
      console.log(err);
      res.send({success: false});
    }
    res.send({success: true});
  });

});
router.post('/login', function(req,res,next){

  passport.authenticate('login', function (err, user, info) {

    if(err) console.log(err);
    if(user){
      // var expires = moment().add('days', 7).valueOf();

      // jwt 토큰 생성
      var token = jwt.encode({
        userCheck: req.body.email,
        // exp: expires
      }, req.app.get("jwtTokenSecret"));

      // response header에 추가
      res.setHeader("token", token)
      res.send({success: true});
    }
    else res.send({success: false});
  })(req,res,next);
});

router.post('/logout', function(req, res){

  console.log("logout jwt토큰 "+ req.body.token);
  var decoded = jwt.decode(req.body.token,req.app.get("jwtTokenSecret"));
  console.log("logout jwt토큰 디코딩 "+ decoded.userCheck);
  var email = decoded.userCheck;

  User.findOne({ email : email }, function(err, user) {
    console.log(user);
    if(err){
      console.log(err);
      res.send({success: false});
    }
    user.pushToken = null;
    console.log('user token is ='+ user.pushToken + '!!');


  });
  res.send({success: true});
});

router.post('/signup', function (req, res, next) {

  passport.authenticate('signup', function (err, user, info) {
    // console.log(user+"s");
    console.log("signUPPPPPPPP");
    if(err) console.log(err);
    if(user) {
      res.send({success: true});
      var userEmail = req.body.email;

      mkdirp('./server/user/'+userEmail+'/video', function (err) {
        if(err) console.log(err);
        else console.log("create dir ./user/" +userEmail );
      }); //server폴더 아래 /user/useremail/video 폴더가 생김.

      //아래 코드는 두 폴더를 만들어버리는 코드.
      // mkdirp('./server/user/'+pathName+'/video/Diet', function (err) {
      //   if(err) console.log(err);
      //   else console.log("create dir ./user/" +pathName );
      // });
      // mkdirp('./server/user/'+pathName+'/video/NoSmoking', function (err) {
      //   if(err) console.log(err);
      //   else console.log("create dir ./user/" +pathName );
      // });
    }
    else res.send({success: false});
  })(req,res,next);
});

//jwt token 사용
router.post('/userInfoEdit', function(req,res){

  User.findOne({email: userEmail}, function(err, user){
    user.password = user.generateHash(req.body.password);
    user.nickname = req.body.nickname;
    user.phoneNumber = req.body.phoneNumber;
    user.imagePath = req.body.imagePath;
  })
})

router.post('/getUserInfo', function (req,res) {

  console.log("get User Info: "+JSON.stringify(req.body));
  console.log("받은 jwt토큰 "+ req.body.token);
  var decoded = jwt.decode(req.body.token, req.app.get("jwtTokenSecret"));
  console.log("받은 jwt토큰 디코딩 "+ decoded.userCheck);
  var email = decoded.userCheck;
  console.log(email);
  User.findOne({email: email}, function(err, info){
    if(err) console.log(err);
    if(info == null) {
      console.log("사용자 아님");
    }
    else {
      console.log("사용자 찾음");
      res.send(info);
    }
  })
})

router.get("/pwdSendMail", function(req, res, next){
  let email = "hwangeyikwon@gmail.com";

  let transporter = nodemailer.createTransport({
    service: 'gmail',
    auth: {
      user: 'hwangeyikwon@gmail.com',  // gmail 계정 아이디를 입력
      pass: ''          // gmail 계정의 비밀번호를 입력
    }
  });
  let mailOptions = {
    from: 'hwangeyikwon@gmail.com',
    to: email,
    subject: '안녕하세요, 모두의 달성입니다. 이메일 인증을 해주세요.',
    html: '<p>새로운 패스워드를 입력 후 아래의 전송 버튼을 클릭해주세요 !</p>' +
      " <form action=\"http://localhost:3000/pwdEmailAuthen\" method=\"post\"> " +
      "<label for=\"pwd\">PW</label>" +
      "  <input type=\"password\" name=\"pwd\" placeholder=\"패스워드 입력\"><br/><br/>" +
      "  <input type=\"submit\" value=\"전송\"> " +
      "</form>"
  };

  transporter.sendMail(mailOptions, function(error, info){
    if (error) {
      console.log(error);
    }
    else {
      console.log('Email sent: ' + info.response);
    }
  });
})

router.post("/pwdEmailAuthen", function(req, res, next){
  console.log(req.body.pwd);

});

router.get("/isParticipated/:jwtToken/:contentName", function(req,res) {
  var decoded = jwt.decode(req.params.jwtToken,req.app.get("jwtTokenSecret"));
  console.log("isParticipated jwt토큰 디코딩 "+ decoded.userCheck);
  var userEmail = decoded.userCheck;

  var contentName = req.params.contentName;

  var joinState;
  var startDate;
  var endDate;
  User.findOne({ email : userEmail , "contentList.contentName": contentName}, function(err, user) {
    if(err){
      console.log(err);
      res.send({success: false});
    }
    else{
      if(user == null) res.send({joinState: 3, startDate: {year: -1, month: -1, day: -1},  endDate: {year: -1, month: -1, day: -1}});
      else  {
        var contentIndex;
        var joinContentCount = user.contentList.length;
        var contentId;

        for (var i = 0; i < joinContentCount; i++) {
          if (user.contentList[i].contentName === contentName) {
            contentIndex = i;
            break;
          }
        }
        joinState = user.contentList[contentIndex].joinState;
        contentId = user.contentList[contentIndex].contentId;

        Content.findOne({name: contentName, id: contentId}, function(err, content){
          startDate = content.startDate;
          endDate = content.endDate;

          res.send({joinState: joinState, startDate: {year: startDate.getFullYear(), month: startDate.getMonth() + 1, day: startDate.getDate()},
            endDate: {year: endDate.getFullYear(), month: endDate.getMonth() + 1, day: endDate.getDate()}});
        });
      }
    }
  });
});

module.exports = router ;