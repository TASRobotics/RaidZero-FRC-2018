'use strict';

const directory = '/home/lvuser/';

const clip = require('clipboardy');
const conn = new require('ssh2').Client();

console.log('Reading data from clipboard...');

const [headerLine, ...rows] = clip.readSync().trim().split('\n');
console.assert(rows.length >= 2, 'Data has less than 3 rows');

const headerCells = headerLine.split('\t');
console.assert(headerCells.length >= 3, 'Copied data has less than 3 columns');
console.assert(headerCells.includes(''), 'Could not find blank column');
console.assert(headerCells.indexOf('') > 3, 'Data for robot has less than 3 columns');
const header = headerCells.slice(2, headerCells.indexOf('')).map(x => x.toLowerCase());
console.assert(header.includes('angle'), 'Could not find angle');
console.assert(header.includes('percent difference'), 'Could not find percent difference');
console.assert(header.includes('left distance'), 'Could not find left distance');
console.assert(header.includes('right distance'), 'Could not find right distance');

const pathInfo = {};
const rowInfo = [];

for (let i = 0; i < rows.length; i++) {
    const [prop, value, ...row] = rows[i].split('\t', header.length + 2);
    if (prop !== '') {
        const prop1 = prop.toLowerCase();
        console.assert(pathInfo[prop1] === undefined, `Duplicate path property ${prop}`);
        console.assert(value !== '', `Property ${prop} has no value`);
        pathInfo[prop1] = value;
    }
    if (row[0] === '?') {
        break;
    }
    const rowObj = {};
    row.forEach((x, j) => {
        console.assert(!Number.isNaN(parseFloat(x)),
            `${x} (row ${i} of ${header[j]}) is not a number`);
        console.assert(rowObj[header[j]] === undefined, `Duplicate column ${header[j]}`);
        rowObj[header[j]] = x;
    });
    rowInfo.push(rowObj);
}

const pathData = [];

console.assert(pathInfo['total distance'] !== undefined,
    `Could not find total distance`);
console.assert(!Number.isNaN(parseFloat(pathInfo['total distance'])),
    `Total distance "${pathInfo['total distance']}" is not a number`);
pathData.push(pathInfo['total distance']);

console.assert(pathInfo.stage !== undefined, `Could not find stage`);
const stage = parseFloat(pathInfo.stage);
console.assert(!Number.isNaN(stage) && stage >= 0,
    `Stage "${pathInfo.stage}" is not a positive number`);
pathData.push(pathInfo.stage);

console.assert(pathInfo.start !== undefined, `Could not find start`);
const start1 = pathInfo.start.toLowerCase();
if (stage === 0) {
    console.assert(['left', 'center', 'right'].includes(start1),
        `Start "${pathInfo.start}" should be left, center, or right for stage 0`);
} else {
    console.assert(['left', 'right'].includes(start1),
        `Start "${pathInfo.start}" should be left or right for stage 1+`);
}
pathData.push(start1);

console.assert(pathInfo.end !== undefined, `Could not find end`);
const end1 = pathInfo.end.toLowerCase();
console.assert(['left', 'right'].includes(end1),
    `End "${pathInfo.end}" should be left or right`);
pathData.push(end1);

const rowData = rowInfo.map(x =>
    [x.angle, x['percent difference'], x['left distance'], x['right distance']]);

const data = pathData.concat(rowData).join('\n');
const filename =
    `${directory}${stage}-${start1}-${end1}.csv`;

console.log('Connecting to robot...');

conn.on('ready', () => {
    console.log('Initializing SFTP...');
    conn.sftp((err, sftp) => {
        if (err) throw err;
        console.log(`Opening file ${filename}...`);
        sftp.open(filename, 'w', (err, handle) => {
            if (err) throw err;
            const buffer = Buffer.from(data);
            console.log('Writing data...');
            sftp.write(handle, buffer, 0, buffer.length, 0, err => {
                if (err) throw err;
                console.log('Successfully wrote data:');
                console.log(data);
                console.log('Closing file...');
                sftp.close(handle, err => {
                    if (err) throw err;
                    conn.end();
                    console.log('Successfully transferred data to robot');
                });
            });
        });
    });
}).connect({
    host: 'roboRIO-4253-FRC.local',
    port: 22,
    username: 'lvuser',
    password: ''
});
