/**
 * Exercici 2 - PR3.1
 * Realitza dues consultes a MongoDB sobre les preguntes inserides per l'exercici 1,
 * mostra el nombre de resultats per pantalla i genera dos informes PDF.
 *
 * Consulta 1: Preguntes amb ViewCount > mitjana de ViewCounts de la col·lecció
 * Consulta 2: Preguntes que contenen en el títol qualsevol de les paraules:
 *             ["pug","wig","yak","nap","jig","mug","zap","gag","oaf","elf"]
 *
 * Autor: Denis
 * Data : 2026-03-17
 */

'use strict';

const fs      = require('fs');
const path    = require('path');
const { MongoClient } = require('mongodb');
const PDFDocument = require('pdfkit');
require('dotenv').config();

// ─── Constants ───────────────────────────────────────────────────────────────
const MONGODB_URI = process.env.MONGODB_URI    || 'mongodb://root:password@localhost:27017/';
const DB_NAME     = process.env.DB_NAME        || 'stackexchange_db';
const COLLECTION  = process.env.COLLECTION_NAME || 'questions';
const OUT_DIR     = process.env.OUT_DIR_PATH   || './data/out';

// Paraules clau per a la consulta 2
const KEYWORDS = ['pug', 'wig', 'yak', 'nap', 'jig', 'mug', 'zap', 'gag', 'oaf', 'elf'];

// ─── Assegurar que el directori de sortida existeixi ─────────────────────────
if (!fs.existsSync(OUT_DIR)) {
  fs.mkdirSync(OUT_DIR, { recursive: true });
}

// ─── Funció: Generar PDF amb títols ─────────────────────────────────────────
/**
 * @param {string}   outputPath  - Ruta on guardar el PDF
 * @param {string}   reportTitle - Títol del informe
 * @param {string}   subtitle    - Subtítol descriptiu de la consulta
 * @param {string[]} titles      - Llista de títols de preguntes
 */
function generatePDF(outputPath, reportTitle, subtitle, titles) {
  return new Promise((resolve, reject) => {
    const doc    = new PDFDocument({ margin: 50, size: 'A4' });
    const stream = fs.createWriteStream(outputPath);

    doc.pipe(stream);

    // ── Capçalera ──────────────────────────────────────────────────────────
    doc
      .fontSize(22)
      .font('Helvetica-Bold')
      .text(reportTitle, { align: 'center' });

    doc.moveDown(0.5);

    doc
      .fontSize(12)
      .font('Helvetica')
      .fillColor('#555555')
      .text(subtitle, { align: 'center' });

    doc.moveDown(0.3);

    // Línia separadora
    doc
      .strokeColor('#cccccc')
      .lineWidth(1)
      .moveTo(50, doc.y)
      .lineTo(545, doc.y)
      .stroke();

    doc.moveDown(0.5);

    // Resum
    doc
      .fontSize(11)
      .fillColor('#333333')
      .font('Helvetica-Bold')
      .text(`Total de resultats: ${titles.length}`, { align: 'left' });

    doc.moveDown(0.8);

    // ── Llista de títols ───────────────────────────────────────────────────
    doc.font('Helvetica').fontSize(10).fillColor('#000000');

    titles.forEach((title, index) => {
      // Nova pàgina si cal
      if (doc.y > 750) {
        doc.addPage();
      }

      doc.text(`${index + 1}. ${title}`, {
        width:   495,
        ellipsis: true,
      });
      doc.moveDown(0.15);
    });

    // Peu de pàgina a l'última pàgina
    doc.moveDown(1);
    doc
      .fontSize(9)
      .fillColor('#888888')
      .text(
        `Generat el ${new Date().toLocaleString('ca-ES')} | PR3.1 - Stack Exchange MongoDB`,
        { align: 'center' }
      );

    doc.end();

    stream.on('finish', () => resolve());
    stream.on('error',  (err) => reject(err));
  });
}

// ─── Consulta 1: ViewCount > middle ─────────────────────────────────────────
async function query1(collection) {
  console.log('\n══════════════════════════════════════════════');
  console.log('  CONSULTA 1: ViewCount > mitjana            ');
  console.log('══════════════════════════════════════════════');

  // Calcular la mitjana de ViewCount amb una agregació
  // Els valors estan emmagatzemats com a strings → convertim a number
  const avgResult = await collection.aggregate([
    {
      $project: {
        viewCountInt: { $toInt: '$question.ViewCount' },
      },
    },
    {
      $group: {
        _id:        null,
        avgCount:   { $avg: '$viewCountInt' },
        totalDocs:  { $sum: 1 },
      },
    },
  ]).toArray();

  if (!avgResult.length) {
    console.log('No hi ha dades a la col·lecció.');
    return [];
  }

  const avgViewCount = avgResult[0].avgCount;
  const totalDocs    = avgResult[0].totalDocs;
  console.log(`Total de documents a la col·lecció: ${totalDocs}`);
  console.log(`Mitjana de ViewCount: ${avgViewCount.toFixed(2)}`);

  // Obtenir les preguntes amb ViewCount > mittana
  const results = await collection.aggregate([
    {
      $project: {
        title:        '$question.Title',
        viewCountInt: { $toInt: '$question.ViewCount' },
      },
    },
    {
      $match: {
        viewCountInt: { $gt: avgViewCount },
      },
    },
    {
      $sort: { viewCountInt: -1 },
    },
  ]).toArray();

  console.log(`Nombre de preguntes amb ViewCount > ${avgViewCount.toFixed(2)}: ${results.length}`);

  return results.map(r => r.title);
}

// ─── Consulta 2: Títol conté alguna de les paraules clau ────────────────────
async function query2(collection) {
  console.log('\n══════════════════════════════════════════════');
  console.log('  CONSULTA 2: Títols amb paraules clau        ');
  console.log('══════════════════════════════════════════════');
  console.log(`Paraules clau cercades: [${KEYWORDS.join(', ')}]`);

  // Construir un filtre $or amb regex per a cada paraula clau
  // La regex és case-insensitive (\i) i cerca la paraula com a subcadena
  const orConditions = KEYWORDS.map(word => ({
    'question.Title': { $regex: word, $options: 'i' },
  }));

  const results = await collection
    .find({ $or: orConditions })
    .project({ 'question.Title': 1, 'question.ViewCount': 1 })
    .sort({ 'question.ViewCount': -1 })
    .toArray();

  console.log(`Nombre de preguntes que contenen alguna de les paraules clau: ${results.length}`);

  return results.map(r => r.question.Title);
}

// ─── Funció principal ────────────────────────────────────────────────────────
async function main() {
  console.log('═══════════════════════════════════════════════════');
  console.log('   EXERCICI 2 - Consultes a MongoDB + PDF         ');
  console.log('═══════════════════════════════════════════════════');

  let client;

  try {
    // Connectar a MongoDB
    console.log(`\nConnectant a MongoDB: ${MONGODB_URI}`);
    client = new MongoClient(MONGODB_URI);
    await client.connect();
    console.log('Connexió a MongoDB establerta correctament.');

    const db         = client.db(DB_NAME);
    const collection = db.collection(COLLECTION);

    // ── Consulta 1 ─────────────────────────────────────────────────────────
    const titles1 = await query1(collection);

    // Generar informe1.pdf
    const pdf1Path = path.join(OUT_DIR, 'informe1.pdf');
    console.log(`\nGenerant ${pdf1Path}...`);
    await generatePDF(
      pdf1Path,
      'Informe 1 - Preguntes amb alt ViewCount',
      `Preguntes amb ViewCount superior a la mitjana de la col·lecció (${titles1.length} resultats)`,
      titles1
    );
    console.log(`✓ ${pdf1Path} generat correctament.`);

    // ── Consulta 2 ─────────────────────────────────────────────────────────
    const titles2 = await query2(collection);

    // Generar informe2.pdf
    const pdf2Path = path.join(OUT_DIR, 'informe2.pdf');
    console.log(`\nGenerant ${pdf2Path}...`);
    await generatePDF(
      pdf2Path,
      'Informe 2 - Preguntes amb paraules clau',
      `Paraules cercades: ${KEYWORDS.join(', ')} (${titles2.length} resultats)`,
      titles2
    );
    console.log(`✓ ${pdf2Path} generat correctament.`);

    console.log('\n═══════════════════════════════════════════════════');
    console.log('   Exercici 2 completat amb èxit!                  ');
    console.log('═══════════════════════════════════════════════════');

  } catch (err) {
    console.error(`\nERROR: ${err.message}`);
    console.error(err.stack);
    process.exit(1);
  } finally {
    if (client) {
      await client.close();
      console.log('Connexió a MongoDB tancada.');
    }
  }
}

// Executar
main();
