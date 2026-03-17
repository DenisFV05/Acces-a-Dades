/**
 * Exercici 1 - PR3.1
 * Llegeix el fitxer Posts.xml de Stack Exchange, extreu les 10.000 preguntes
 * amb més ViewCount, decodifica les entitats HTML del camp Body, i les insereix
 * a MongoDB en el format requerit.
 *
 * Autor: Denis
 * Data : 2026-03-17
 */

'use strict';

const fs      = require('fs');
const path    = require('path');
const xml2js  = require('xml2js');
const he      = require('he');
const { MongoClient } = require('mongodb');
const winston = require('winston');
require('dotenv').config();

// ─── Constants ───────────────────────────────────────────────────────────────
const XML_FILE_PATH  = process.env.XML_FILE_PATH  || './data/Posts.xml';
const MONGODB_URI    = process.env.MONGODB_URI     || 'mongodb://root:password@localhost:27017/';
const DB_NAME        = process.env.DB_NAME         || 'stackexchange_db';
const COLLECTION     = process.env.COLLECTION_NAME || 'questions';
const LOG_FILE_PATH  = process.env.LOG_FILE_PATH   || './data/logs';
const MAX_INSERTS    = 10000;

// ─── Assegurar que el directori de logs existeixi ────────────────────────────
if (!fs.existsSync(LOG_FILE_PATH)) {
  fs.mkdirSync(LOG_FILE_PATH, { recursive: true });
}

// ─── Configuració del logger (Winston) ───────────────────────────────────────
const logger = winston.createLogger({
  level: process.env.LOG_LEVEL || 'info',
  format: winston.format.combine(
    winston.format.timestamp({ format: 'YYYY-MM-DD HH:mm:ss' }),
    winston.format.printf(({ timestamp, level, message }) =>
      `[${timestamp}] ${level.toUpperCase()}: ${message}`
    )
  ),
  transports: [
    // Sortida per pantalla
    new winston.transports.Console(),
    // Sortida a fitxer de log
    new winston.transports.File({
      filename: path.join(LOG_FILE_PATH, 'exercici1.log'),
      maxsize:  process.env.LOG_MAX_SIZE  || '20m',
      maxFiles: process.env.LOG_MAX_FILES || '14d',
    }),
  ],
});

// ─── Funció: Parsejar el fitxer XML ─────────────────────────────────────────
async function parseXML(filePath) {
  logger.info(`Llegint el fitxer XML: ${filePath}`);
  const xmlData = fs.readFileSync(filePath, 'utf-8');

  const parser = new xml2js.Parser({
    explicitArray: false,
    mergeAttrs:    true,   // Els atributs XML passen a propietats planes
  });

  return new Promise((resolve, reject) => {
    parser.parseString(xmlData, (err, result) => {
      if (err) reject(err);
      else     resolve(result);
    });
  });
}

// ─── Funció: Processar i filtrar les dades ──────────────────────────────────
function processData(data) {
  // xml2js: data.posts.row és l'array de files (o un objecte si només n'hi ha una)
  const rows = data.posts.row;
  const allRows = Array.isArray(rows) ? rows : [rows];

  logger.info(`Total de registres al XML: ${allRows.length}`);

  // Filtrar únicament preguntes (PostTypeId === "1")
  const questions = allRows.filter(row => row.PostTypeId === '1');
  logger.info(`Total de preguntes (PostTypeId=1): ${questions.length}`);

  // Ordenar per ViewCount descendent i agafar el top 10.000
  questions.sort((a, b) => {
    const va = parseInt(a.ViewCount || '0', 10);
    const vb = parseInt(b.ViewCount || '0', 10);
    return vb - va;
  });

  const top = questions.slice(0, MAX_INSERTS);
  logger.info(`Preguntes seleccionades (top ${MAX_INSERTS}): ${top.length}`);

  // Construir el document MongoDB per a cada pregunta
  return top.map(row => {
    // Decodificar les entitats HTML del camp Body
    // Exemple: "&#x3C;p&#x3E;" → "<p>"
    const decodedBody = row.Body ? he.decode(row.Body) : '';

    return {
      question: {
        Id:               row.Id              || '',
        PostTypeId:       row.PostTypeId      || '',
        AcceptedAnswerId: row.AcceptedAnswerId || '',
        CreationDate:     row.CreationDate    || '',
        Score:            row.Score           || '0',
        ViewCount:        row.ViewCount       || '0',
        Body:             decodedBody,
        OwnerUserId:      row.OwnerUserId     || row.OwnerDisplayName || '',
        LastActivityDate: row.LastActivityDate || '',
        Title:            row.Title           || '',
        Tags:             row.Tags            || '',
        AnswerCount:      row.AnswerCount     || '0',
        CommentCount:     row.CommentCount    || '0',
        ContentLicense:   row.ContentLicense  || '',
      },
    };
  });
}

// ─── Funció principal ────────────────────────────────────────────────────────
async function main() {
  logger.info('═══════════════════════════════════════════════════');
  logger.info('   EXERCICI 1 - Inserció de dades a MongoDB        ');
  logger.info('═══════════════════════════════════════════════════');

  let client;

  try {
    // 1. Parsejar el XML
    const xmlData = await parseXML(path.resolve(XML_FILE_PATH));

    // 2. Processar i filtrar les dades
    logger.info('Processant les dades...');
    const documents = processData(xmlData);

    // 3. Connectar a MongoDB
    logger.info(`Connectant a MongoDB: ${MONGODB_URI}`);
    client = new MongoClient(MONGODB_URI);
    await client.connect();
    logger.info('Connexió a MongoDB establerta correctament.');

    const db         = client.db(DB_NAME);
    const collection = db.collection(COLLECTION);

    // 4. Esborrar documents existents per evitar duplicats
    logger.info(`Esborrant documents existents de la col·lecció "${COLLECTION}"...`);
    const deleteResult = await collection.deleteMany({});
    logger.info(`Documents esborrats: ${deleteResult.deletedCount}`);

    // 5. Inserir els nous documents
    logger.info(`Inserint ${documents.length} documents a MongoDB...`);
    const insertResult = await collection.insertMany(documents);
    logger.info(`Documents inserits correctament: ${insertResult.insertedCount}`);

    // 6. Verificació ràpida: mostrar la primera pregunta inserida
    const sample = await collection.findOne({});
    if (sample) {
      logger.info(`Exemple de document inserit → Id: ${sample.question.Id}, Títol: "${sample.question.Title}", ViewCount: ${sample.question.ViewCount}`);
    }

    logger.info('Exercici 1 completat amb èxit!');

  } catch (err) {
    logger.error(`Error durant l'execució: ${err.message}`);
    logger.error(err.stack);
    process.exit(1);
  } finally {
    if (client) {
      await client.close();
      logger.info('Connexió a MongoDB tancada.');
    }
  }
}

// Executar
main();
