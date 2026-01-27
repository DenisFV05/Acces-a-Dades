// src/models/SentimentAnalysis.js
const { DataTypes } = require('sequelize');
const { sequelize } = require('../config/database');

const SentimentAnalysis = sequelize.define('SentimentAnalysis', {
  id: {
    type: DataTypes.UUID,
    defaultValue: DataTypes.UUIDV4,
    primaryKey: true
  },
  text: {
    type: DataTypes.TEXT,
    allowNull: false
  },
  sentiment: {
    type: DataTypes.STRING, // positive | negative | neutral | error
    allowNull: false
  },
  meta: {
    type: DataTypes.JSON, // opcional: datos adicionales (score, rawResponse, userId, gameId...)
    allowNull: true
  }
}, {
  tableName: 'sentiment_analysis',
  timestamps: true
});

module.exports = SentimentAnalysis;
