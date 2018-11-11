const mongoose = require('mongoose');

const { Schema } = mongoose;
const contentSchema = new Schema({
  id: String,
  roomNum : Number,
  startDate: Date,
  endDate: Date,
  achievementRate: Number,
  userList: Array,
  isDone: Number,
});

module.exports = mongoose.model('Content', contentSchema);
